package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.UserIdDocument;



import java.util.UUID;


public interface UserIdDocumentService {
    void verifyDocument(UUID documentId);
    void rejectDocument(UUID documentId);
    void createDocument(User user, String frontId, String backId);
    void updateDocument(UUID documentId, String documentType, String documentNumber, String documentImage);
    void deleteDocument(UUID documentId);
    UserIdDocument getDocument(UUID documentId);
    UserIdDocument getDocuments(UUID userId);

}
