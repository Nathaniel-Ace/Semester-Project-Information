package com.dms.service;

import com.dms.messaging.MessageProducer;
import com.dms.persistence.entity.Document;
import com.dms.persistence.repo.DocumentRepo;
import com.dms.service.mapper.DocumentMapper;
import com.dms.service.dto.DocumentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentMapper documentMapper;
    private final DocumentRepo documentRepo;
    private final MessageProducer messageProducer;
    private final MinioService minioService;

    public DocumentDTO saveDocument(DocumentDTO documentDTO, MultipartFile file) {
        try {
            // 1. Upload file to MinIO
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            long fileSize = file.getSize();
            String contentType = file.getContentType();

            String bucketName = "mindease-bucket";
            minioService.uploadFile(bucketName, fileName, inputStream, fileSize, contentType);

            // 2. Generate URL for accessing the uploaded file
            String fileUrl = String.format("http://localhost:9000/%s/%s", bucketName, fileName);
            documentDTO.setFileUrl(fileUrl);

            // 3. Convert DTO to Entity and save it in the database
            Document document = documentMapper.toEntity(documentDTO);
            Document savedDocument = documentRepo.save(document);

            // 4. Prepare a message for RabbitMQ
            String message = String.format("Document uploaded: [ID: %d, Title: %s]",
                    savedDocument.getId(), savedDocument.getTitle());

            // 5. Send the message to RabbitMQ
            messageProducer.sendMessage("documentExchange", "documentRoutingKey", message);

            // 6. Return the saved document DTO
            return documentMapper.toDTO(savedDocument);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload document", e);
        }
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
