package com.gin.wegd.auth_service.jwts;

import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.comon.TokenType;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.models.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
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
        TokenType tokenType = TokenType.ACCESS_TOKEN;
        return buildToken(extraClaims, user, jwtExpiration, tokenType);
    }

    public String generateRefreshToken(User user) {
        TokenType tokenType = TokenType.REFRESH_TOKEN;
        return buildToken(new HashMap<>(), user, refreshExpiration, tokenType);
    }

    public String generateTmpToken(User user) {
        TokenType tokenType = TokenType.TMP_TOKEN;
        return buildTmpToken(user, jwtExpiration, tokenType);
    }

    private String buildTmpToken( User user, long expiration, TokenType tokenType) {
        try {

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUserName())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expiration))
                    .claim("id", user.getId())
                    .claim("jti", UUID.randomUUID().toString())
                    .claim("type", tokenType)
                    .build();

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(new MACSigner(getSignInKey()));

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new CustomException(ErrorCode.COURSER_NOT_EXISTED);
        }
    }

    private String buildToken(Map<String, Object> extraClaims, User user, long expiration, TokenType tokenType) {
        try {
            List<String> authorities = user.getRole().stream().map(Role::name).toList();
            extraClaims.put("roles", authorities);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUserName())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expiration))
                    .claim("roles", authorities)
                    .claim("id", user.getId())
                    .claim("jti", UUID.randomUUID().toString())
                    .claim("email", user.getEmail())
                    .claim("phone", user.getPhone())
                    .claim("type", tokenType)
                    .build();

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(new MACSigner(getSignInKey()));

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new CustomException(ErrorCode.COURSER_NOT_EXISTED);
        }
    }

    private boolean isTokenValid(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(getSignInKey());
            if (!signedJWT.verify(verifier)) {
                return false;
            }
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return !expirationTime.before(new Date());
        } catch (JOSEException | ParseException e) {
            return false;
        }
    }

    private TokenType getTokenType(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String type = signedJWT.getJWTClaimsSet().getStringClaim("type");

            return TokenType.valueOf(type);
        } catch (ParseException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public boolean isTokenInvalidAndType(String token, TokenType tokenType) {
        return !isTokenValid(token) || !getTokenType(token).equals(tokenType);

    }
    public Map<String, Object> extractClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            return claimsSet.getClaims();
        } catch (ParseException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }


    public boolean isTmpKeyValid(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(getTmpKey());
            if (!signedJWT.verify(verifier)) {
                return false;
            }
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return !expirationTime.before(new Date());
        } catch (JOSEException | ParseException e) {
            return false;
        }
    }

    private byte[] getTmpKey() {
        return Decoders.BASE64.decode(secretTmpKey);
    }
    private byte[] getSignInKey() {
        return Decoders.BASE64.decode(secretKey);
    }
}