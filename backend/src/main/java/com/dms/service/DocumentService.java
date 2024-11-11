package com.dms.service;

import com.dms.messaging.MessageProducer;
import com.dms.persistence.entity.Document;
import com.dms.persistence.repo.DocumentRepo;
import com.dms.service.mapper.DocumentMapper;
import com.dms.service.dto.DocumentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentMapper documentMapper;
    private final DocumentRepo documentRepo;
    private final MessageProducer messageProducer;

    public DocumentDTO saveDocument(DocumentDTO documentDTO, MultipartFile file) {
        // 1. Simulate a file URL response for now
        String simulatedUrl = "http://localhost:8080/files/" + file.getOriginalFilename();
        documentDTO.setFileUrl(simulatedUrl);

        // 2. Convert DTO to Entity and save it
        Document document = documentMapper.toEntity(documentDTO);
        Document savedDocument = documentRepo.save(document);

        // 3. Prepare the message for RabbitMQ
        String message = String.format("Document uploaded: [ID: %d, Title: %s]",
                savedDocument.getId(), savedDocument.getTitle());

        // 4. Send message to RabbitMQ
        messageProducer.sendMessage("documentExchange", "documentRoutingKey", message);

        // 5. Return the saved document DTO
        return documentMapper.toDTO(savedDocument);
    }

    public Iterable<DocumentDTO> findAllDocuments() {
        return documentMapper.toDTOList(documentRepo.findAll());
    }

    public DocumentDTO findDocumentById(Long id) {
        return documentRepo.findById(id).map(documentMapper::toDTO).orElse(null);
    }

    public DocumentDTO findDocumentByName(String name) {
        return documentMapper.toDTO(documentRepo.findByTitle(name));
    }

    public DocumentDTO updateDocument(Long id, DocumentDTO updatedDocument) {
        return documentRepo.findById(id).map(document -> {
            document.setTitle(updatedDocument.getTitle());
            document.setFileUrl(updatedDocument.getFileUrl());
            document.setDescription(updatedDocument.getDescription());
            return documentMapper.toDTO(documentRepo.save(document));
        }).orElse(null);
    }

    public void deleteDocumentById(Long id) {
        // Check if the document exists
        if (documentRepo.existsById(id)) {
            documentRepo.deleteById(id);
        } else {
            throw new IllegalArgumentException("Document with ID " + id + " does not exist.");
        }
    }
}
