package com.example.jooby.infra.security.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
// Importe este
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
// Você não precisa mais do JwtGrantedAuthoritiesConverter aqui
// import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter; 

// import java.util.Collection; // Não é mais necessário

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserDetailsService userDetailsService;
    
    // O JwtGrantedAuthoritiesConverter não é mais necessário aqui,
    // pois vamos usar as authorities do nosso UserDetails
    // private final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public JwtAuthConverter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // 1. Busca seu usuário customizado no banco
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());

        // 2. Retorna um token de autenticação que usa o seu UserDetails como principal
        return new UsernamePasswordAuthenticationToken(
                userDetails, // <-- O "principal" agora é o seu objeto CustomUserDetails
                null,        // <-- Credenciais (senha) não são necessárias, o JWT já validou
                userDetails.getAuthorities() // <-- Pega as roles/authorities do seu UserDetails
        );
    }
}