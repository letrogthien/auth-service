package com.gin.wegd.auth_service.controllers;


import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.UtilsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/v1/io")
@RequiredArgsConstructor
public class IOController {
    private final UtilsService utilsService;

    @PostMapping("/img/")
    @ResponseStatus(HttpStatus.OK)
    public byte[] getImg(@RequestParam String img) {
        return ApiResponse.<byte[]>builder()
                .message("Image retrieved successfully")
                .data(utilsService.getImageBytes(img))
                .build()
                .getData();
    }
}
