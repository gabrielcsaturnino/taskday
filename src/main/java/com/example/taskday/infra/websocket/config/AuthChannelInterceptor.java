package com.example.taskday.infra.websocket.config;

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

import com.example.taskday.infra.security.service.JwtService;
import com.example.taskday.user.CustomUserDetails;
import com.example.taskday.user.User;
import com.example.taskday.user.service.UserDetailsServiceImpl;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {
    private final JwtDecoder jwtDecoder;
    
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    
    public AuthChannelInterceptor(@Lazy JwtDecoder jwtDecoder, @Lazy UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtDecoder = jwtDecoder;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // Intercepta apenas o comando de conexão para autenticar.
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // O frontend deve enviar o token no cabeçalho 'Authorization' do STOMP.
            List<String> authorization = accessor.getNativeHeader("Authorization");
            
            if (authorization == null || authorization.isEmpty()) {
                System.err.println("Error: Authorization header is missing.");
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
                        CustomUserDetails userDetails = (CustomUserDetails) this.userDetailsServiceImpl.loadUserByUsername(username);
                        System.err.println("User authenticated: " + userDetails.getUsername());
                        // Cria o objeto de autenticação do Spring Security.
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        
                        // Associa o usuário autenticado à sessão WebSocket.
                        // A partir daqui, o 'Principal' estará disponível nos seus controllers.
                        accessor.setUser(authentication);
                    }
                } catch (JwtException e) {
                    // Se o token for inválido, a conexão não será autenticada.
                    System.err.println("Error: Invalid JWT token: ");
                }
            }
        }
        return message;
    }
}
