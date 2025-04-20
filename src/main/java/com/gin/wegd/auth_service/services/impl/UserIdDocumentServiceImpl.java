package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.comon.UserIdDocStatus;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.UserIdDocument;
import com.gin.wegd.auth_service.repositories.UserIdDocumentRepository;
import com.gin.wegd.auth_service.services.UserIdDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserIdDocumentServiceImpl implements UserIdDocumentService {
    private final UserIdDocumentRepository userIdDocumentRepository;
    @Override
    public void verifyDocument(UUID documentId) {
        UserIdDocument userIdDocument = this.getDocument(documentId);
        userIdDocument.setStatus(UserIdDocStatus.VERIFIED);
        userIdDocumentRepository.save(userIdDocument);
    }

    @Override
    public void rejectDocument(UUID documentId) {
        UserIdDocument userIdDocument = this.getDocument(documentId);
        userIdDocument.setStatus(UserIdDocStatus.REJECTED);
        userIdDocumentRepository.save(userIdDocument);
    }

    @Override
    public void createDocument(User user, String frontId, String backId) {
        UserIdDocument userIdDocument = new UserIdDocument();
        userIdDocument.setUser(user);
        userIdDocument.setFrontId(frontId);
        userIdDocument.setBackId(backId);
        userIdDocument.setStatus(UserIdDocStatus.PENDING);
        userIdDocumentRepository.save(userIdDocument);
    }


    @Override
    public UserIdDocument getDocument(UUID documentId) {
        return userIdDocumentRepository.findById(documentId).orElseThrow(
                () -> new RuntimeException("Document not found")
        );
    }

    @Override
    public UserIdDocument getDocuments(UUID userId) {
        return userIdDocumentRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public List<UserIdDocument> getAllUserIdDocumentsByStatus(UserIdDocStatus userIdDocStatus) {
        return userIdDocumentRepository.findAllByStatus(userIdDocStatus);
    }
}
