package com.dms.service.impl;

import com.dms.api.DocumentController;
import com.dms.exception.DocumentStorageException;
import com.dms.messaging.MessageProducer;
import com.dms.messaging.OCRJobProducer;
import com.dms.persistence.entity.Document;
import com.dms.persistence.repo.DocumentRepo;
import com.dms.service.DocumentService;
import com.dms.service.MinioService;
import com.dms.service.dto.OCRJobDTO;
import com.dms.service.mapper.DocumentMapper;
import com.dms.service.dto.DocumentDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentMapper documentMapper;
    private final DocumentRepo documentRepo;
    private final MessageProducer messageProducer;
    private final MinioService minioService;
    private final OCRJobProducer ocrJobProducer;

    @Override
    public void processOCRJob(DocumentDTO documentDTO) {
        try {
            // OCRJobDTO erstellen
            OCRJobDTO ocrJobDTO = new OCRJobDTO();
            ocrJobDTO.setDocumentId(documentDTO.getId());
            ocrJobDTO.setDocumentTitle(documentDTO.getTitle());
            ocrJobDTO.setFileUrl(documentDTO.getFileUrl());
            ocrJobDTO.setPageCount(documentDTO.getPageCount());

            // OCR-Job an RabbitMQ senden
            ocrJobProducer.sendOCRJob("OCR_QUEUE", ocrJobDTO);
        } catch (Exception e) {
            throw new DocumentStorageException("Failed to process OCR job", e);
        }
    }

    @Override
    public DocumentDTO saveDocument(DocumentDTO documentDTO, MultipartFile file) {
        try {
            // 1. Datei in MinIO hochladen
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            long fileSize = file.getSize();
            String contentType = file.getContentType();

            String bucketName = "mindease-bucket";
            logger.info("Uploading file to MinIO: {}", fileName);
            minioService.uploadFile(bucketName, fileName, inputStream, fileSize, contentType);

            // 2. URL für den Zugriff auf die hochgeladene Datei generieren
            String fileUrl = String.format("http://localhost:9000/%s/%s", bucketName, fileName);
            logger.info("File uploaded successfully to MinIO: {}", fileUrl);
            documentDTO.setFileUrl(fileUrl);

            // 3. DTO in Entity konvertieren und in der Datenbank speichern
            logger.info("Saving document to database: {}", documentDTO.getTitle());
            Document document = documentMapper.toEntity(documentDTO);
            Document savedDocument = documentRepo.save(document);

            // 4. RabbitMQ-Nachricht vorbereiten
            logger.info("Preparing message for RabbitMQ: {}", savedDocument.getTitle());
            String message = String.format("Document uploaded: [ID: %d, Title: %s, Pages: %d]",
                    savedDocument.getId(), savedDocument.getTitle(), documentDTO.getPageCount());

            // 5. Nachricht an RabbitMQ senden
            logger.info("Sending message to RabbitMQ: {}", message);
            messageProducer.sendMessage("documentExchange", "documentRoutingKey", message);

            // 6. Gespeichertes Dokument als DTO zurückgeben
            logger.info("Document saved successfully: {}", savedDocument.getTitle());
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

    @Override
    public DocumentDTO updateDocument(Long id, DocumentDTO updatedDocument) {
        try {
            return documentRepo.findById(id).map(document -> {
                document.setTitle(updatedDocument.getTitle());
                document.setFileUrl(updatedDocument.getFileUrl());
                document.setContent(updatedDocument.getContent());
                document.setPageCount(updatedDocument.getPageCount());
                return documentMapper.toDTO(documentRepo.save(document));
            }).orElse(null);
        } catch (Exception e) {
            throw new DocumentStorageException("Failed to update document", e);
        }
    }

    @Override
    public void updateDocumentContent(Long documentId, String ocrText) {
        try {
            documentRepo.findById(documentId).ifPresentOrElse(document -> {
                document.setContent(ocrText);
                document.setUpdatedAt(LocalDateTime.now());
                documentRepo.save(document);
                System.out.println("Document content updated successfully for ID: " + documentId);
            }, () -> {
                throw new DocumentStorageException("Document with ID " + documentId + " not found.");
            });
        } catch (Exception e) {
            throw new DocumentStorageException("Failed to update document content", e);
        }
    }

    @Override
    public void deleteDocumentById(Long id) {
        // Check if the document exists
        if (documentRepo.existsById(id)) {
            documentRepo.deleteById(id);
        } else {
            throw new IllegalArgumentException("Document with ID " + id + " does not exist.");
        }
    }

}
