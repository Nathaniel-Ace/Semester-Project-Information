package com.dms.service.dto;

import lombok.Data;

@Data
public class OCRResultDTO {
    private Long documentId;
    private String ocrText;
}
