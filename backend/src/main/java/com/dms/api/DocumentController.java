package com.dms.api;

import com.dms.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dms.persistence.dto.DocumentDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService documentService;


    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> saveDocument(@RequestBody DocumentDTO documentDTO) {
        return ResponseEntity.ok(documentService.saveDocument(documentDTO));
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
            return ResponseEntity.noContent().build();  // HTTP 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();  // HTTP 404 Not Found
        }
    }
}
