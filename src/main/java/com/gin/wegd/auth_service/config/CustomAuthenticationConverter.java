package com.gin.wegd.auth_service.config;

import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.comon.TokenType;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final UserService userService;
    private final RedisTemplate<String, String> stringRedisTemplate;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        if (jwt.getClaim("type").equals(TokenType.REFRESH_TOKEN.toString())) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
        List<GrantedAuthority> roles = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt,roles);
    }

    private List<GrantedAuthority> extractAuthorities(Jwt jwt) {
        if (jwt.getClaim("iss")!=null && jwt.getIssuer().toString().equals("https://accounts.google.com")) {
            return extractAuthoritiesFromGGToken(jwt);
        }
        return extractAuthoritiesFromToken(jwt);
    }



    private List<GrantedAuthority> extractAuthoritiesFromToken(Jwt jwt) {
        List<GrantedAuthority> roles = new ArrayList<>();
        if (jwt.getClaim("roles") != null) {
            List<String> roleList = jwt.getClaim("roles");
            roles = roleList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return roles;
    }

    private List<GrantedAuthority> extractAuthoritiesFromGGToken(Jwt jwt) {
        List<GrantedAuthority> roles = new ArrayList<>();
        String email = jwt.getClaim("email");
        if (email == null || email.isEmpty()) {
            return roles;
        }
        User user = userService.getUserByEmail(email);
        if (user == null || user.getRole() == null || user.getRole().isEmpty()) {
            return roles;
        }
        roles = user.getRole().stream().map(Role::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return roles;
    }
}
