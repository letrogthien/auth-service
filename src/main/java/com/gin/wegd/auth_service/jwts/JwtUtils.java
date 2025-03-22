package com.gin.wegd.auth_service.jwts;

import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.comon.TokenType;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.models.User;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;


@Service
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${security.secret.key}")
    private String secretKey;

    @Value("${security.secret.tmpKey}")
    private String secretTmpKey;

    @Value("${security.secret.expiration}")
    private long jwtExpiration;

    @Value("${security.secret.refreshToken}")
    private long refreshExpiration;

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtExpiration, TokenType.ACCESS_TOKEN);
    }

    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), user, refreshExpiration, TokenType.REFRESH_TOKEN);
    }

    public String generateTmpToken(User user) {
        return buildToken(new HashMap<>(), user, jwtExpiration, TokenType.TMP_TOKEN);
    }

    private String buildToken(Map<String, Object> extraClaims, User user, long expiration, TokenType tokenType) {
        try {
            List<String> authorities = user.getRole().stream()
                    .map(Role::name)
                    .toList();
            extraClaims.put("roles", authorities);
            extraClaims.put("id", user.getId());
            extraClaims.put("jti", UUID.randomUUID().toString());
            extraClaims.put("email", user.getEmail());
            extraClaims.put("phone", user.getPhone());
            extraClaims.put("type", tokenType.toString());

            Key key = (tokenType == TokenType.TMP_TOKEN) ? get2FaSecretKey() : getSecretKey();
            return Jwts.builder().setClaims(extraClaims)
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_SIGNATURE);
        }
    }

    public boolean isTokenValid(String token) {
        return verifyToken(token);
    }



    private boolean verifyToken(String token) {
        try {
            String tokenType = getTokenType(token);
            Key key = (tokenType.equals(TokenType.TMP_TOKEN.toString())) ? get2FaSecretKey() : getSecretKey();
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTokenType(String token) {
        Map<String, Object> claims = extractClaims(token);
        return claims.get("type").toString();
    }

    public Map<String, Object> extractClaims(String token) {
        try {
            JWT jwt = JWTParser.parse(token);
            return jwt.getJWTClaimsSet().getClaims();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }



    private Key getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Key get2FaSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretTmpKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}