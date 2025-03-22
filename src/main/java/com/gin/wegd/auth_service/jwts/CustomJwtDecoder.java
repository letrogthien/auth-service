package com.gin.wegd.auth_service.jwts;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.text.ParseException;


@Service
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${security.secret.key}")
    private String secretKey;
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            JWT jwt = JWTParser.parse(token);
            if (jwt.getJWTClaimsSet().getIssuer() != null) {
                String issuer = jwt.getJWTClaimsSet().getIssuer();
                if (issuer.contains("google")) {
                    return googleJwtDecoder().decode(token);
                }
            }
            return jwtDecoder().decode(token);
        } catch (ParseException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    private JwtDecoder jwtDecoder() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    private JwtDecoder googleJwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
    }
}
