package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.dtos.UserDto;
import com.gin.wegd.auth_service.models.responses.ApiResponse;


import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserByEmail(final String email);
    User createUser(final User user);
    void deleteUser(final UUID userId);
    User getUserById(final UUID id);
    User getUserByUsername(final String username);
    List<User> getAllUsers();
    void saveUser(final User user);
    boolean exitsByEmail(String email);
    boolean exitsByUserName(String userName);
    void banUser(UUID userId);
    List<String> getAllUserNames();
    void addUsernameToCache(String username);
    void removeUsernameFromCache(String username);
    void unbanUser(UUID userId);


    ApiResponse<UserDto> getUserDetailsByUsername(String userName);
    ApiResponse<List<String>>searchUserByPrefix(String keyword);
    ApiResponse<List<UserDto>> getAllUser();
    ApiResponse<List<String>> getAllUserWithRole(Role roleName);
    ApiResponse<UserDto> getUserDetailsById(UUID userId);

}
