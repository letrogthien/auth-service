package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UtilsService {
    private static final String UPLOAD_DIR = "uploads/kyc/";

    private String saveFile(MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String filePath = UPLOAD_DIR + fileName;
            Path path = Paths.get(filePath);
            Files.write(path, file.getBytes());

            return filePath;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.IO_EXCEPTION);
        }
    }

    public String saveKycFile(MultipartFile file) {
        return saveFile(file);
    }
}
