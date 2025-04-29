package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUserName(String userName);

    boolean existsUserByEmail(String email);
    boolean existsUserByUserName(String userName);


    @Query(value = "SELECT username FROM users", nativeQuery = true)
    List<String> getAllUserNames();

    @Query(value = """
    SELECT u.username 
    FROM users u
    JOIN user_roles ur ON u.id = ur.user_id
    JOIN roles r ON ur.role_id = r.id
    WHERE r.name = ?1
    """, nativeQuery = true)
    List<String> getAllUserNamesWithRole(Role role);

}
