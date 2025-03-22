package com.gin.wegd.auth_service.config;


import com.gin.wegd.auth_service.jwts.CustomJwtDecoder;
import com.gin.wegd.auth_service.jwts.JwtUtils;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class Security {
    private final CustomJwtDecoder jwtDecoder;
    private final CustomAuthenticationConverter converter;
    private final CustomAuthenticationEntryPoint entryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.csrf(AbstractHttpConfigurer::disable);
        security.authorizeHttpRequests(req ->
                req.requestMatchers(
                        "api/v1/auth/login").permitAll()
                        .requestMatchers("api/v1/auth/test").authenticated()
                        .anyRequest().permitAll());
        security.sessionManagement(s ->
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        security.oauth2ResourceServer(
                oauth2 ->
                    oauth2.jwt(jwtSpec-> jwtSpec.decoder(jwtDecoder)
                            .jwtAuthenticationConverter(converter))
                            .authenticationEntryPoint(entryPoint)
        );
        return security.build();
    }



}
