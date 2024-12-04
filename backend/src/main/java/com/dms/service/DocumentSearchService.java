// src/main/java/com/dms/service/DocumentSearchService.java
package com.dms.service;

import com.dms.service.dto.DocumentSearchDTO;

import java.util.List;

public interface DocumentSearchService {
    List<DocumentSearchDTO> findAllDocuments();
    DocumentSearchDTO findDocumentById(Long id);
    List<DocumentSearchDTO> searchDocuments(String query);
}