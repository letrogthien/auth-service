package com.gin.wegd.auth_service.controllers;


import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/search")
public class SearchController {
    private final UserService userService;

    @GetMapping("/user/name")
    public ApiResponse<List<String>> searchByPrefix(@RequestParam String keyword) {
        return userService.searchUserByPrefix(keyword);
    }


}
