package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.comon.OtpPurpose;
import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.comon.TokenStatus;
import com.gin.wegd.auth_service.comon.TokenType;
import com.gin.wegd.auth_service.kafka.producer.ProducerService;
import com.gin.wegd.auth_service.models.user_attribute.UserStatus;
import com.gin.wegd.auth_service.config.CustomPasswordEncoder;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.jwts.JwtUtils;
import com.gin.wegd.auth_service.mapper.UserMapper;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.requests.*;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.models.responses.LoginResponse;
import com.gin.wegd.auth_service.models.responses.RegisterResponse;
import com.gin.wegd.auth_service.redis.RedisUtils;
import com.gin.wegd.auth_service.services.AuthService;
import com.gin.wegd.auth_service.services.OtpService;
import com.gin.wegd.auth_service.services.UserService;
import com.gin.wegd.common.events.RegisterEvModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final CustomPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;
    private final UserMapper userMapper;
    private final ProducerService producerService;
    private final RedisUtils redisUtils;
    private final OtpService otpService;


    @Override
    public ApiResponse<LoginResponse> login(LoginRq loginRequest) {
        User user = userService.getUserByUsername(loginRequest.getAccountName());
        validatePassword(loginRequest.getPassword(), user.getPassword());
        validateUserStatus(user.getStatus());

        if (user.isTwoFactorAuthEnabled()) {
            return handleTwoFactorAuth(user);
        }

        return generateAndStoreTokens(user);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.passwordEncoder().matches(rawPassword, encodedPassword)) {
            throw new CustomException(ErrorCode.USERNAME_OR_PASSWORD_FAIL);
        }
    }

    private void validateUserStatus(UserStatus status) {
        if (status.equals(UserStatus.INACTIVE)) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private ApiResponse<LoginResponse> handleTwoFactorAuth(User user) {
        otpService.generateOtp(user, OtpPurpose.TWO_FACTOR_AUTHENTICATION);
        String tmpToken = jwtUtil.generateTmpToken(user);
        return ApiResponse.<LoginResponse>builder()
                .data(LoginResponse.builder()
                        .status("2FA")
                        .tmpToken(tmpToken)
                        .build())
                .message("Login success")
                .build();
    }

    private ApiResponse<LoginResponse> generateAndStoreTokens(User user) {
        String token = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        String key = generateRefreshKey(jwtUtil.extractClaims(token));
        redisUtils.setRefreshToken(key, TokenStatus.ACTIVE.toString());
        return ApiResponse.<LoginResponse>builder()
                .data(LoginResponse.builder()
                        .status("success")
                        .token(token)
                        .refreshToken(refreshToken)
                        .build())
                .message("Login success")
                .build();
    }

    private void validateEmailAndUsername(String email, String username) {
        if (userService.exitsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_EXISTED);
        }
        if (userService.exitsByUserName(username)) {
            throw new CustomException(ErrorCode.USERNAME_INVALID);
        }

    }

    @Override
    @Transactional
    public ApiResponse<RegisterResponse> register(RegisterRq registerRequest) {
        validateEmailAndUsername(registerRequest.getEmail(), registerRequest.getUserName());
        User u = createUser(registerRequest);
        RegisterEvModel registerEvModel = RegisterEvModel.newBuilder()
                .setUserName(u.getUserName())
                .setEmail(u.getEmail())
                .build();
        producerService.registerEv(registerEvModel);
        return ApiResponse.<RegisterResponse>builder()
                .data(RegisterResponse.builder()
                        .message("register success")
                        .build())
                .message("Register success")
                .build();
    }

    private User createUser(RegisterRq request) {
        User user = userMapper.userDtoToUser(request);
        encodePassword(user, request.getPassword());
        assignDefaultRole(user);
        userService.saveUser(user);
        return user;
    }

    private void encodePassword(User user, String rawPassword) {
        user.setPassword(passwordEncoder.passwordEncoder().encode(rawPassword));
        user.setStatus(UserStatus.ACTIVE);
    }

    private void assignDefaultRole(User user) {
        List<Role> roles = new LinkedList<>();
        roles.add(Role.USER);
        user.setRole(roles);
    }



    @Override
    public ApiResponse<LoginResponse> refreshToken(String refreshToken) {
        if (jwtUtil.isTokenInvalidAndType(refreshToken, TokenType.REFRESH_TOKEN)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Map<String, Object> claims = jwtUtil.extractClaims(refreshToken);
        String key =claims.get("id").toString()+":"+ claims.get("jti").toString();
        if (redisUtils.refreshTokenInactive(key)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String userId = claims.get("id").toString();
        User user = userService.getUserById(userId);
        String token = jwtUtil.generateToken(user);


        return ApiResponse.<LoginResponse>builder()
                .data(LoginResponse.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .build())
                .message("Refresh token success")
                .build();
    }

    @Override
    public ApiResponse<String> logout(String refreshToken) {
        if (jwtUtil.isTokenInvalidAndType(refreshToken, TokenType.REFRESH_TOKEN)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Map<String, Object> claims = jwtUtil.extractClaims(refreshToken);
        String key =claims.get("id").toString()+":"+ claims.get("jti").toString();
        redisUtils.setRefreshToken(key, TokenStatus.INACTIVE.toString());
        return ApiResponse.<String>builder()
                .message("logout success")
                .build();
    }

    @Override
    public ApiResponse<String> logoutAll(String refreshToken) {
        if (jwtUtil.isTokenInvalidAndType(refreshToken, TokenType.REFRESH_TOKEN)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        Map<String, Object> claims = jwtUtil.extractClaims(refreshToken);
        String userId = claims.get("id").toString();
        redisUtils.invalidateUserTokens(userId + ":*");
        return ApiResponse.<String>builder()
                .message("logout success")
                .build();
    }

    @Override
    public ApiResponse<LoginResponse> verify2Fa(Verify2FaRq rq) {
        jwtUtil.isTmpKeyValid(rq.getToken());
        Map<String, Object> claims = jwtUtil.extractClaims(rq.getToken());
        String userId = claims.get("id").toString();
        otpService.getOtpValid(userId, rq.getOtp(), OtpPurpose.TWO_FACTOR_AUTHENTICATION);
        User user = userService.getUserById(userId);
        String token = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        String key =this.generateRefreshKey(claims);
        redisUtils.setRefreshToken(key, TokenStatus.ACTIVE.toString());
        return ApiResponse.<LoginResponse>builder()
                .data(LoginResponse.builder()
                        .status("success")
                        .token(token)
                        .refreshToken(refreshToken)
                        .build())
                .message("2FA success")
                .build();
    }

    private String generateRefreshKey(Map<String, Object> claims) {
        String userId = Optional.ofNullable(claims.get("id"))
                .map(Object::toString)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
        String jti = Optional.ofNullable(claims.get("jti"))
                .map(Object::toString)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
        return userId + ":" + jti;
    }

}
