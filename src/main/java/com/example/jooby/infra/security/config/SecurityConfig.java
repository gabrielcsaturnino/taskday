package com.example.jooby.infra.security.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
   private final JwtAuthConverter jwtAuthConverter;

public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
    this.jwtAuthConverter = jwtAuthConverter;
}



    @Bean
    @Order(1)
    SecurityFilterChain websocketSecurity(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/ws/**")
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable()); 
        return http.build();
    }


    @Bean
@Order(2)
SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/authenticate", "/api/v1/clients", "/api/v1/contractors").permitAll()
            .requestMatchers("/api/v1/jobs/active", "/api/v1/jobs/search").permitAll()
            .requestMatchers("/api/v1/contractors/search").permitAll()
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .anyRequest().authenticated() // mudar para exigir autenticação
        )
        .oauth2ResourceServer(oauth2 -> 
            oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
        );

    return http.build();
}




    @Bean
    JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }


    @Bean
    JwtEncoder jwtEncoder(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
         var jwk = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .build();

         var jsonWebKeySet = new ImmutableJWKSet<>(new JWKSet(jwk));        
        return new NimbusJwtEncoder(jsonWebKeySet);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
