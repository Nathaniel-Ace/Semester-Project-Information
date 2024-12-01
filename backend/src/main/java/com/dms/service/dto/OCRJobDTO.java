package com.dms.service.dto;

import lombok.Data;

@Data
public class OCRJobDTO {
    private Long documentId;
    private String documentTitle;
    private String fileUrl;
    private String metadata;
}
