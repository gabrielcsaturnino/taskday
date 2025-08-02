package com.example.taskday.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.example.taskday.domain.model.CustomUserDetails;
import com.example.taskday.domain.model.User;
import com.example.taskday.domain.service.JwtService;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {
    private final JwtDecoder jwtDecoder;
    
    private final UserDetailsService userDetailsService;

    
    public AuthChannelInterceptor(@Lazy JwtDecoder jwtDecoder, @Lazy UserDetailsService userDetailsService) {
        this.jwtDecoder = jwtDecoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // Intercepta apenas o comando de conexão para autenticar.
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // O frontend deve enviar o token no cabeçalho 'Authorization' do STOMP.
            List<String> authorization = accessor.getNativeHeader("Authorization");
            
            if (authorization == null || authorization.isEmpty()) {
                // Se não houver token, a conexão prossegue, mas não será autenticada.
                return message;
            }
            
            String bearerToken = authorization.get(0);
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                
                try {
                    // Decodifica o token usando o JwtDecoder.
                    Jwt jwt = jwtDecoder.decode(token);
                    
                    // Extrai o 'subject' do token, que é o username (email no seu caso).
                    String username = jwt.getSubject();

                    if (username != null) {
                        // Carrega os detalhes do usuário usando seu UserDetailsServiceImpl.
                        CustomUserDetails userDetails = (CustomUserDetails) this.userDetailsService.loadUserByUsername(username);
                        
                        // Cria o objeto de autenticação do Spring Security.
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        
                        // Associa o usuário autenticado à sessão WebSocket.
                        // A partir daqui, o 'Principal' estará disponível nos seus controllers.
                        accessor.setUser(authentication);
                    }
                } catch (JwtException e) {
                    // Se o token for inválido, a conexão não será autenticada.
                    System.err.println("Falha na autenticação do WebSocket: Token JWT inválido. " + e.getMessage());
                }
            }
        }
        return message;
    }
}
