package com.example.taskday.domain.service;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
   private final JwtEncoder jwtEncoder;

   public JwtService(JwtEncoder jwtEncoder) {
      this.jwtEncoder = jwtEncoder;
   }
   
   public String generateToken(Authentication authentication) {
      Instant now = Instant.now();
      Instant expiresAt = now.plusSeconds(3600); 

      String scope = authentication.getAuthorities()
         .stream()
         .map(GrantedAuthority::getAuthority)
         .collect(Collectors.joining(" "));

      var claims = JwtClaimsSet.builder()
         .issuer("https://taskday.example.com")
         .issuedAt(now)
         .expiresAt(expiresAt)
         .subject(authentication.getName())
         .claim("scope", scope)
         .build();
   
      return jwtEncoder.encode(JwtEncoderParameters.from(claims))
         .getTokenValue();
   }

}
