package com.gin.wegd.auth_service.config;


import com.gin.wegd.auth_service.jwts.CustomJwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

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
                        HttpMethod.GET,
                        "auth/login",
                        "auth/register").permitAll()
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
