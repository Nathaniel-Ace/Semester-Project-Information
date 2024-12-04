package com.dms.service;

import com.dms.service.dto.DocumentDTO;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    DocumentDTO saveDocument(DocumentDTO documentDTO, MultipartFile file);
    void processOCRJob(DocumentDTO documentDTO);
    DocumentDTO updateDocument(Long id, DocumentDTO updatedDocument);
    void updateDocumentContent(Long documentId, String ocrText);
    void deleteDocumentById(Long id);
}