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
@CrossOrigin(origins = "http://localhost") // Erlaubt nur Anfragen von diesem Origin
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentSearchService documentSearchService;

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> saveDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("pageCount") int pageCount) { // Neuer Parameter pageCount

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setTitle(title);
        documentDTO.setPageCount(pageCount); // Seitenanzahl hinzufügen

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
        // Über DocumentSearchService suchen
        return ResponseEntity.ok(documentSearchService.findAllDocuments());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<DocumentSearchDTO> findDocumentById(@PathVariable Long id) {
        // Über DocumentSearchService suchen
        return ResponseEntity.ok(documentSearchService.findDocumentById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DocumentSearchDTO>> searchDocuments(@RequestParam String query) {
        // Suche in Elasticsearch nach OCR-Text und Titeln
        return ResponseEntity.ok(documentSearchService.searchDocuments(query));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable Long id, @RequestBody DocumentDTO updatedDocument) {
        return ResponseEntity.ok(documentService.updateDocument(id, updatedDocument));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable Long id) {
        try {
            documentService.deleteDocumentById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private final ElasticsearchTestService elasticsearchTestService;

    @GetMapping("/test-elasticsearch")
    public ResponseEntity<String> testElasticsearchConnection() {
        String result = elasticsearchTestService.testConnection();
        return ResponseEntity.ok(result);
    }
}
