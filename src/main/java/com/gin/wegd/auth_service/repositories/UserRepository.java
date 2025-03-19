package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByEmail(String email);

    Optional<Void> deleteByEmail(String email);

    Optional<User> findUserByUserName(String userName);
    boolean existsUserByEmail(String email);
    boolean existsUserByUserName(String userName);
}
