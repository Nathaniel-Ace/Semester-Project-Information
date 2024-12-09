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

        long startTime = System.currentTimeMillis(); // Startzeit messen
        logger.info("Initiating document upload: title={}, pageCount={}", title, pageCount); // Eingangslog

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setTitle(title);
        documentDTO.setPageCount(pageCount);

        try {
            // Datei speichern
            logger.info("Saving document file: {}", file.getOriginalFilename());
            DocumentDTO savedDocument = documentService.saveDocument(documentDTO, file);
            logger.debug("Document {} successfully saved in database", savedDocument.getId()); // Erfolgsmeldung nach Datenbank-Speicherung

            // OCR-Job initiieren
            logger.info("Initiating OCR job for document: {}", savedDocument.getId());
            documentService.processOCRJob(savedDocument);
            logger.info("OCR job successfully initiated for document {}", savedDocument.getId()); // Erfolgsmeldung für OCR-Job

            logger.info("Document upload completed in {} ms", System.currentTimeMillis() - startTime); // Performance-Log
            return ResponseEntity.ok(savedDocument);
        } catch (Exception e) {
            logger.error("Error occurred while saving document: {}", e.getMessage(), e); // Fehler-Log
            throw new DocumentStorageException("Failed to save document", e);
        }
    }

    @GetMapping("/find/all")
    public ResponseEntity<List<DocumentSearchDTO>> findAllDocuments() {
        logger.info("Fetching all documents from the database"); // Eingangslog
        try {
            List<DocumentSearchDTO> documents = documentSearchService.findAllDocuments();
            logger.info("Successfully fetched {} documents", documents.size()); // Erfolgsmeldung mit Anzahl der Dokumente
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error fetching documents: {}", e.getMessage(), e); // Fehler-Log
            throw new DocumentStorageException("Failed to fetch documents", e);
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<DocumentSearchDTO> findDocumentById(@PathVariable Long id) {
        logger.info("Fetching document with ID: {}", id); // Eingangslog
        try {
            DocumentSearchDTO document = documentSearchService.findDocumentById(id);
            logger.debug("Document fetched successfully: {}", document); // Detail-Log bei erfolgreichem Abruf
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            logger.error("Error fetching document with ID {}: {}", id, e.getMessage(), e); // Fehler-Log
            throw new DocumentStorageException("Failed to fetch document", e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<DocumentSearchDTO>> searchDocuments(@RequestParam String query) {
        logger.info("Searching documents with query: {}", query); // Eingangslog
        try {
            List<DocumentSearchDTO> results = documentSearchService.searchDocuments(query);
            logger.info("Search completed with {} results", results.size()); // Erfolgsmeldung mit Anzahl der Ergebnisse
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error during document search: {}", e.getMessage(), e); // Fehler-Log
            throw new DocumentStorageException("Failed to search documents", e);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable Long id, @RequestBody DocumentDTO updatedDocument) {
        logger.info("Updating document with ID: {}", id); // Eingangslog
        try {
            DocumentDTO updated = documentService.updateDocument(id, updatedDocument);
            logger.info("Document {} updated successfully", id); // Erfolgsmeldung
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Error updating document with ID {}: {}", id, e.getMessage(), e); // Fehler-Log
            throw new DocumentStorageException("Failed to update document", e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable Long id) {
        logger.warn("Attempting to delete document with ID: {}", id); // Warnung vor Löschaktion
        try {
            documentService.deleteDocumentById(id);
            logger.info("Document {} deleted successfully", id); // Erfolgsmeldung
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Document with ID {} not found: {}", id, e.getMessage()); // Warnung bei nicht vorhandenem Dokument
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error deleting document with ID {}: {}", id, e.getMessage(), e); // Fehler-Log
            throw new DocumentStorageException("Failed to delete document", e);
        }
    }

    @GetMapping("/test-elasticsearch")
    public ResponseEntity<String> testElasticsearchConnection() {
        logger.info("Testing Elasticsearch connection"); // Eingangslog
        try {
            String result = elasticsearchTestService.testConnection();
            logger.info("Elasticsearch connection successful: {}", result); // Erfolgsmeldung
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error testing Elasticsearch connection: {}", e.getMessage(), e); // Fehler-Log
            throw new DocumentStorageException("Failed to test Elasticsearch connection", e);
        }
    }
}