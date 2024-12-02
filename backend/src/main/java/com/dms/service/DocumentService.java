package com.dms.service;

import com.dms.messaging.MessageProducer;
import com.dms.messaging.OCRJobProducer;
import com.dms.persistence.entity.Document;
import com.dms.persistence.repo.DocumentRepo;
import com.dms.service.dto.OCRJobDTO;
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
    private final OCRJobProducer ocrJobProducer;

    public void processOCRJob(DocumentDTO documentDTO) {
        // OCRJobDTO erstellen
        OCRJobDTO ocrJobDTO = new OCRJobDTO();
        ocrJobDTO.setDocumentId(documentDTO.getId());
        ocrJobDTO.setDocumentTitle(documentDTO.getTitle());
        ocrJobDTO.setFileUrl(documentDTO.getFileUrl());
        ocrJobDTO.setPageCount(documentDTO.getPageCount());

        // OCR-Job an RabbitMQ senden
        ocrJobProducer.sendOCRJob("OCR_QUEUE", ocrJobDTO);
    }

    public DocumentDTO saveDocument(DocumentDTO documentDTO, MultipartFile file) {
        try {
            // 1. Datei in MinIO hochladen
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            long fileSize = file.getSize();
            String contentType = file.getContentType();

            String bucketName = "mindease-bucket";
            minioService.uploadFile(bucketName, fileName, inputStream, fileSize, contentType);

            // 2. URL für den Zugriff auf die hochgeladene Datei generieren
            String fileUrl = String.format("http://localhost:9000/%s/%s", bucketName, fileName);
            documentDTO.setFileUrl(fileUrl);

            // 3. DTO in Entity konvertieren und in der Datenbank speichern
            Document document = documentMapper.toEntity(documentDTO);
            Document savedDocument = documentRepo.save(document);

            // 4. RabbitMQ-Nachricht vorbereiten
            String message = String.format("Document uploaded: [ID: %d, Title: %s, Pages: %d]",
                    savedDocument.getId(), savedDocument.getTitle(), documentDTO.getPageCount());

            // 5. Nachricht an RabbitMQ senden
            messageProducer.sendMessage("documentExchange", "documentRoutingKey", message);

            // 6. Gespeichertes Dokument als DTO zurückgeben
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
            document.setPageCount(updatedDocument.getPageCount());
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
