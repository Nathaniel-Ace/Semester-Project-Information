package com.dms.messaging;

import com.dms.service.DocumentService;
import com.dms.service.dto.OCRResultDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OCRResultListener {

    private final DocumentService documentService;

    public OCRResultListener(DocumentService documentService) {
        this.documentService = documentService;
    }

    @RabbitListener(queues = "RESULT_QUEUE")
    public void receiveOCRResult(OCRResultDTO ocrResultDTO) {
        System.out.println("OCR Result received:");

        // Aktualisiere die Beschreibung und updatedAt in der Datenbank
        try {
            documentService.updateDocumentContent(ocrResultDTO.getDocumentId(), ocrResultDTO.getOcrText());
            System.out.println("Document updated successfully with OCR text for ID: " + ocrResultDTO.getDocumentId());
        } catch (Exception e) {
            System.err.println("Failed to update document for ID: " + ocrResultDTO.getDocumentId());
            e.printStackTrace();
        }
    }
}
