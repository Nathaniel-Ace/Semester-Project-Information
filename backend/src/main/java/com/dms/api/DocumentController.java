package com.dms.api;

import com.dms.persistence.entities.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    // Hardcoded list of documents
    private final List<Document> documents = Arrays.asList(
            new Document(1L, "Document 1", "This is document 1"),
            new Document(2L, "Document 2", "This is document 2"),
            new Document(3L, "Document 3", "This is document 3")
    );

    // Save a new document (hardcoded response)
    @PostMapping("/save")
    public ResponseEntity<Document> saveDocument(@RequestBody Document document) {
        // Here, return a hardcoded document as a response
        Document hardcodedDocument = new Document(4L, "Hardcoded Document", "This is a hardcoded response.");
        return new ResponseEntity<>(hardcodedDocument, HttpStatus.CREATED);
    }

    // Get all documents (hardcoded response)
    @GetMapping("/all")
    public ResponseEntity<List<Document>> findAllDocuments() {
        // Return the hardcoded list of documents
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    // Get a document by ID (hardcoded response)
    @GetMapping("/{id}")
    public ResponseEntity<Document> findDocumentById(@PathVariable Long id) {
        // Look for the document in the hardcoded list
        Optional<Document> document = documents.stream().filter(doc -> doc.getId().equals(id)).findFirst();
        return document.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete a document by ID (hardcoded response)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable Long id) {
        // Simulate the delete operation (no real deletion happens in hardcoded responses)
        Optional<Document> document = documents.stream().filter(doc -> doc.getId().equals(id)).findFirst();
        if (document.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
