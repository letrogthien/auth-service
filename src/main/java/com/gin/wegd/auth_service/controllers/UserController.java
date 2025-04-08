package com.gin.wegd.auth_service.controllers;

import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //get user by username
    @GetMapping("/detail")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<User> getUserByUsername(@RequestParam String username){
        return userService.getUserDetailsByUsername(username);
    }

}
