package com.dms.api;

import com.dms.exception.DocumentStorageException;
import com.dms.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dms.service.dto.DocumentDTO;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
@CrossOrigin(origins = "http://localhost") // Erlaubt nur Anfragen von diesem Origin
public class DocumentController {
    private final DocumentService documentService;

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

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
    public ResponseEntity<Iterable<DocumentDTO>> findAllDocuments() {
        return ResponseEntity.ok(documentService.findAllDocuments());
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<DocumentDTO> findDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.findDocumentById(id));
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<DocumentDTO> findDocumentByName(@PathVariable String name) {
        return ResponseEntity.ok(documentService.findDocumentByName(name));
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
}
