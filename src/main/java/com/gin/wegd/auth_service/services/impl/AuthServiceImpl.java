package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.comon.OtpPurpose;
import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.comon.TokenStatus;
import com.gin.wegd.auth_service.comon.TokenType;
import com.gin.wegd.auth_service.kafka.producer.ProducerService;
import com.gin.wegd.auth_service.models.OtpModel;
import com.gin.wegd.auth_service.models.Roles;
import com.gin.wegd.auth_service.models.StrangeDevice;
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
import com.gin.wegd.auth_service.redis.StrangeDeviceCacheService;
import com.gin.wegd.auth_service.repositories.RoleRepository;
import com.gin.wegd.auth_service.services.AuthService;
import com.gin.wegd.auth_service.services.OtpService;
import com.gin.wegd.auth_service.services.UserService;
import com.gin.wegd.common.events.RegisterEvModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private final RoleRepository roleRepository;
    private final StrangeDeviceCacheService strangeDeviceCacheService;


    @Override
    @Transactional
    public ApiResponse<LoginResponse> login(LoginRq loginRequest) {
        User user = userService.getUserByUsername(loginRequest.getAccountName());
        validatePassword(loginRequest.getPassword(), user.getPassword());
        validateUserStatus(user.getStatus());
        if (user.isTwoFactorAuthEnabled()|| isStrangeDevice(user.getId(),loginRequest.getDeviceInfo())) {
            return handleTwoFactorAuth(user);
        }
        return generateTokensAndCache(user);
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

    private boolean isStrangeDevice(UUID id,StrangeDevice device) {
        return strangeDeviceCacheService.isStrangeDevice(id, device);
    }

    private ApiResponse<LoginResponse> handleTwoFactorAuth(User user) {
        otpService.createAndSendOtp(user.getEmail(),user.getUserName(),user.getId(),OtpPurpose.TWO_FACTOR_AUTHENTICATION);
        String tmpToken = jwtUtil.generateTmpToken(user);
        return ApiResponse.<LoginResponse>builder()
                .data(LoginResponse.builder()
                        .status("2FA")
                        .tmpToken(tmpToken)
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
        userService.addUsernameToCache(u.getUserName());
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
        List<Roles> roles = new LinkedList<>();
        roleRepository.findByName(Role.USER)
                .ifPresent(roles::add);
        user.setRole(roles);
    }


    @Override
    public ApiResponse<LoginResponse> refreshToken(String refreshToken) {
        if (!isRefreshTokenValid(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        Map<String, Object> claims = jwtUtil.extractClaims(refreshToken);
        String keyCache = generateCacheRefreshKey(claims);
        if (redisUtils.refreshTokenIsInactive(keyCache)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String userId = claims.get("id").toString();
        User user = userService.getUserById(UUID.fromString(userId));
        String token = jwtUtil.generateToken(user);


        return ApiResponse.<LoginResponse>builder()
                .data(LoginResponse.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .build())
                .message("Refresh token success")
                .build();
    }

    private boolean isRefreshTokenValid(String refreshToken) {
        return jwtUtil.isTokenValid(refreshToken) &&
                Objects.equals(jwtUtil.getTokenType(refreshToken), TokenType.REFRESH_TOKEN.toString());
    }

    @Override
    public ApiResponse<String> logout(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String keyCache = generateCacheRefreshKey(jwtUtil.extractClaims(refreshToken));
        redisUtils.setRefreshToken(keyCache, TokenStatus.INACTIVE.toString());
        return ApiResponse.<String>builder()
                .message("logout success")
                .build();
    }

    @Override
    public ApiResponse<String> logoutAll(String refreshToken) {
        if (isRefreshTokenValid(refreshToken)) {
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
        if (!jwtUtil.isTokenValid(rq.getToken())) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
        Map<String, Object> claims = jwtUtil.extractClaims(rq.getToken());
        if (!claims.get("type").equals(TokenType.TMP_TOKEN.toString())) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
        UUID userId = UUID.fromString((String) claims.get("id"));
        OtpModel otpModel= otpService.getOtpValid(userId, rq.getOtp(), OtpPurpose.TWO_FACTOR_AUTHENTICATION);
        otpService.deleteOtp(otpModel);
        User user = userService.getUserById(userId);
        return generateTokensAndCache(user);
    }

    @Override
    public ApiResponse<String> trustDevice(StrangeDevice strangeDevice) {
        UUID userId = extractUserIdInContext();
        strangeDeviceCacheService.addStrangeDevice(userId, strangeDevice);
        return ApiResponse.<String>builder()
                .message("Trust device success")
                .build();
    }


    private String generateCacheRefreshKey(Map<String, Object> claims) {
        String userId = Optional.ofNullable(claims.get("id"))
                .map(Object::toString)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
        String jti = Optional.ofNullable(claims.get("jti"))
                .map(Object::toString)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));

        return userId + ":" + jti;
    }
    private ApiResponse<LoginResponse> generateTokensAndCache (User user) {
        String token = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        String keyCache = this.generateCacheRefreshKey(jwtUtil.extractClaims(refreshToken));
        redisUtils.setRefreshToken(keyCache, TokenStatus.ACTIVE.toString());
        return ApiResponse.<LoginResponse>builder()
                .data(LoginResponse.builder()
                        .status("success")
                        .token(token)
                        .refreshToken(refreshToken)
                        .build())
                .message("Login success")
                .build();
    }


    private UUID extractUserIdInContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        return UUID.fromString(jwt.getClaim("id"));
    }


}
