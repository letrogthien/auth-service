package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.config.CustomPasswordEncoder;
import com.gin.wegd.auth_service.mapper.UserMapper;
import com.gin.wegd.auth_service.models.Roles;
import com.gin.wegd.auth_service.models.dtos.UserDto;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.models.user_attribute.UserStatus;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.repositories.RoleRepository;
import com.gin.wegd.auth_service.repositories.UserRepository;
import com.gin.wegd.auth_service.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserMapper userMapper;
    private static final String USERNAME_KEY = "usernames";
    private final CustomPasswordEncoder customPasswordEncoder;
    private final RoleRepository roleRepository;


    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_EXISTED)
        );
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }




    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_EXISTED)
        );
        userRepository.delete(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(()->
                        new CustomException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public User getUserByUsername(String username) {
        return  userRepository.findUserByUserName(username).orElseThrow(
                ()-> new CustomException(ErrorCode.USER_NOT_EXISTED)
        );

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean exitsByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean exitsByUserName(String userName) {
        return userRepository.existsUserByUserName(userName);
    }

    @Override
    public void banUser(UUID userId) {
        User u= userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_EXISTED));
        u.setStatus(UserStatus.BANNED);
        userRepository.save(u);
    }


    @Override
    public List<String> getAllUserNames() {
        return userRepository.getAllUserNames();
    }

    @Override
    public ApiResponse<UserDto> getUserDetailsByUsername(String userName) {
        UserDto userDto = userMapper.userToUserDto(this.getUserByUsername(userName));
        return ApiResponse.<UserDto>builder()
                .data(userDto)
                .build();
    }

    @Override
    public ApiResponse<List<String>> searchUserByPrefix(String keyword) {
        Set<String> usernames = redisTemplate.opsForSet().members(USERNAME_KEY);
        List<String> result = Optional.ofNullable(usernames)
                .orElse(Collections.emptySet())
                .stream()
                .filter(username -> username.contains(keyword))
                .toList();
        return ApiResponse.<List<String>>builder()
                .data(result)
                .message("Search success")
                .build();
    }

    @PostConstruct
    public void cacheUsernames() {
        List<String> usernames = this.getAllUserNames();
        usernames.forEach(username -> redisTemplate.opsForSet().add(USERNAME_KEY, username));
    }
    @Override
    public void addUsernameToCache(String username) {
        redisTemplate.opsForSet().add(USERNAME_KEY, username);
    }

    @Override
    public void removeUsernameFromCache(String username) {
        redisTemplate.opsForSet().remove(USERNAME_KEY, username);
    }

    @Override
    public void unbanUser(UUID userId) {
        User u= userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_EXISTED));
        u.setStatus(UserStatus.ACTIVE);
        userRepository.save(u);
    }

    @Override
    public ApiResponse<List<UserDto>> getAllUser() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtoList = users.stream()
                .map(userMapper::userToUserDto)
                .toList();
        return ApiResponse.<List<UserDto>>builder()
                .data(userDtoList)
                .message("Users retrieved successfully")
                .build();
    }

    @Override
    public ApiResponse<List<String>> getAllUserWithRole(Role roleName) {
        return ApiResponse.<List<String>>builder()
                .data(userRepository.getAllUserNamesWithRole(roleName))
                .message("Users retrieved successfully")
                .build();
    }

    @Override
    public ApiResponse<UserDto> getUserDetailsById(UUID userId) {
        UserDto userDto = userMapper.userToUserDto(this.getUserById(userId));
        return ApiResponse.<UserDto>builder()
                .data(userDto)
                .message("User retrieved successfully")
                .build();
    }

    @PostConstruct
    public void init() {
        if (!userRepository.existsUserByUserName("admin")) {
            createAdmin();
        }
    }

    private void createAdmin(){
        List<Roles> roles = roleRepository.findAll();
        User user = User.builder()
                .password(customPasswordEncoder.passwordEncoder().encode("123123"))
                .email("admin@gmail.com")
                .role(roles)
                .userName("admin").build();
        userRepository.save(user);
    }

}
