package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.models.User;



import java.util.List;

public interface UserService {
    User getUserByEmail(final String email);
    User createUser(final User user);
    void deleteUser(final String userId);
    User getUserById(final String id);
    User getUserByUsername(final String username);
    List<User> getAllUsers();
    void saveUser(final User user);
    boolean exitsByEmail(String email);
    boolean exitsByUserName(String userName);
    void banUser(String userId);
}
