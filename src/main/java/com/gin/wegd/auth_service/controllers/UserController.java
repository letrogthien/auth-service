package com.gin.wegd.auth_service.controllers;

import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.kafka.producer.impl.ProducerServiceImpl;
import com.gin.wegd.auth_service.models.dtos.UserDto;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ProducerServiceImpl producerService;

    //get user by username
    @GetMapping("/detail")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserDto> getUserByUsername(@RequestParam String username){
        return userService.getUserDetailsByUsername(username);
    }

    //get all user
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<UserDto>> getAllUser(){
        return userService.getAllUser();
    }

    //get all user by role
    @GetMapping("/all/role")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<String>> getAllUserByRole(@RequestParam Role roleName){
        return userService.getAllUserWithRole(roleName);
    }

    //get user by id
    @GetMapping("/detail/id")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserDto> getUserById(@RequestParam UUID userId){
        return userService.getUserDetailsById(userId);
    }
}
