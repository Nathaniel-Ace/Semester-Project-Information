//package com.dms.messaging;
//
//import com.dms.service.dto.OCRJobDTO;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OCRJobListener {
//
//    @RabbitListener(queues = "OCR_QUEUE")
//    public void receiveOCRJob(OCRJobDTO ocrJobDTO) {
//        System.out.println("OCR-Job empfangen:");
//        System.out.println("Document ID: " + ocrJobDTO.getDocumentId());
//        System.out.println("Document Title: " + ocrJobDTO.getDocumentTitle());
//        System.out.println("File URL: " + ocrJobDTO.getFileUrl());
//        System.out.println("Page Count: " + ocrJobDTO.getPageCount());
//
//        // Weiterverarbeitung des OCR-Jobs, z. B. Übergabe an OCR-Service
//    }
//}
