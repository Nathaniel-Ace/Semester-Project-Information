package com.dms.api;

import com.dms.exception.DocumentStorageException;
import com.dms.service.DocumentSearchService;
import com.dms.service.DocumentService;
import com.dms.service.ElasticsearchTestService;
import com.dms.service.dto.DocumentSearchDTO;
import com.dms.service.dto.DocumentDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
@CrossOrigin(origins = "http://localhost")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentSearchService documentSearchService;
    private final ElasticsearchTestService elasticsearchTestService;

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> saveDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("pageCount") int pageCount) {

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setTitle(title);
        documentDTO.setPageCount(pageCount);

        logger.info("Saving document with title: {} and page count: {}", title, pageCount);

        try {
            // Datei speichern
            DocumentDTO savedDocument = documentService.saveDocument(documentDTO, file);

            // OCR-Job initiieren
            documentService.processOCRJob(savedDocument);

            logger.info("Document saved successfully with page count: {}", pageCount);
            return ResponseEntity.ok(savedDocument);
        } catch (Exception e) {
            logger.error("Error saving document: {}", e.getMessage());
            throw new DocumentStorageException("Failed to save document", e);
        }
    }

    @GetMapping("/find/all")
    public ResponseEntity<List<DocumentSearchDTO>> findAllDocuments() {
        try {
            logger.info("Finding all documents");
            return ResponseEntity.ok(documentSearchService.findAllDocuments());
        } catch (Exception e) {
            logger.error("Error finding all documents: {}", e.getMessage());
            throw new DocumentStorageException("Failed to find all documents", e);
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<DocumentSearchDTO> findDocumentById(@PathVariable Long id) {
        try {
            logger.info("Finding document by id: {}", id);
            return ResponseEntity.ok(documentSearchService.findDocumentById(id));
        } catch (Exception e) {
            logger.error("Error finding document by id: {}", e.getMessage());
            throw new DocumentStorageException("Failed to find document by id", e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<DocumentSearchDTO>> searchDocuments(@RequestParam String query) {
        try {
            logger.info("Searching documents with query: {}", query);
            return ResponseEntity.ok(documentSearchService.searchDocuments(query));
        } catch (Exception e) {
            logger.error("Error searching documents: {}", e.getMessage());
            throw new DocumentStorageException("Failed to search documents", e);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable Long id, @RequestBody DocumentDTO updatedDocument) {
        try {
            return ResponseEntity.ok(documentService.updateDocument(id, updatedDocument));
        } catch (Exception e) {
            logger.error("Error updating document: {}", e.getMessage());
            throw new DocumentStorageException("Failed to update document", e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable Long id) {
        try {
            documentService.deleteDocumentById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting document: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error deleting document: {}", e.getMessage());
            throw new DocumentStorageException("Failed to delete document", e);
        }
    }

    @GetMapping("/test-elasticsearch")
    public ResponseEntity<String> testElasticsearchConnection() {
        try {
            String result = elasticsearchTestService.testConnection();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error testing Elasticsearch connection: {}", e.getMessage());
            throw new DocumentStorageException("Failed to test Elasticsearch connection", e);
        }
    }
}