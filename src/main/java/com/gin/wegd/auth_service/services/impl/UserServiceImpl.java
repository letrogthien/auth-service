package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.models.user_attribute.UserStatus;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.mapper.UserMapper;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.repositories.UserRepository;
import com.gin.wegd.auth_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


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
        return userRepository.findUserByUserName(username).orElseThrow(
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
}
