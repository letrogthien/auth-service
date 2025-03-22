package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.services.AdminService;
import com.gin.wegd.auth_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserService userService;
    @Override
    public void banUserById(UUID userId) {
        userService.banUser(userId);
    }
}
