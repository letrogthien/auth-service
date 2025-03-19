package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.comon.UserIdDocStatus;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.UserIdDocument;
import com.gin.wegd.auth_service.repositories.UserIdDocumentRepository;
import com.gin.wegd.auth_service.services.UserIdDocumentService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

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
    public void updateDocument(UUID documentId, String documentType, String documentNumber, String documentImage) {

    }

    @Override
    public void deleteDocument(UUID documentId) {

    }

    @Override
    public UserIdDocument getDocument(UUID documentId) {
        return userIdDocumentRepository.findById(documentId).orElseThrow(
                () -> new RuntimeException("Document not found")
        );
    }

    @Override
    public UserIdDocument getDocuments(UUID userId) {
        return userIdDocumentRepository.findByUserId(userId);
    }
}
