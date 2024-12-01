package com.dms.service.mapper;

import com.dms.messaging.OCRJob;
import com.dms.service.dto.OCRJobDTO;

public class OCRJobMapper {

    // Konvertiere von DTO zu OCRJob
    public static OCRJob toEntity(OCRJobDTO dto) {
        OCRJob ocrJob = new OCRJob();
        ocrJob.setDocumentId(dto.getDocumentId());
        ocrJob.setDocumentTitle(dto.getDocumentTitle());
        ocrJob.setFileUrl(dto.getFileUrl());
        ocrJob.setMetadata(dto.getMetadata());
        return ocrJob;
    }

    // Konvertiere von OCRJob zu DTO
    public static OCRJobDTO toDTO(OCRJob ocrJob) {
        OCRJobDTO dto = new OCRJobDTO();
        dto.setDocumentId(ocrJob.getDocumentId());
        dto.setDocumentTitle(ocrJob.getDocumentTitle());
        dto.setFileUrl(ocrJob.getFileUrl());
        dto.setMetadata(ocrJob.getMetadata());
        return dto;
    }
}
