package com.gin.wegd.auth_service.config;


import com.gin.wegd.auth_service.jwts.CustomJwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        security.authorizeHttpRequests(req -> req
                .requestMatchers("/api/v1/auth/login",
                        "/api/v1/auth/register",
                        "/api/v1/auth/verify-2fa",
                        "/api/v1/search/user/name",
                        "/api/v1/userCenter/forget-password",
                        "/api/v1/user/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**")
                .permitAll()
                .anyRequest().authenticated());
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
