package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.comon.UserIdDocStatus;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.UserIdDocument;


import java.util.List;
import java.util.UUID;


public interface UserIdDocumentService {
    void verifyDocument(UUID documentId);
    void rejectDocument(UUID documentId);
    void createDocument(User user, String frontId, String backId);
    UserIdDocument getDocument(UUID documentId);
    UserIdDocument getDocuments(UUID userId);

    List<UserIdDocument> getAllUserIdDocumentsByStatus(UserIdDocStatus userIdDocStatus);
}
