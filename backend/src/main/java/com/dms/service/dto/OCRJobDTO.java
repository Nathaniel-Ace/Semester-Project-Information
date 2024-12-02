package com.dms.service.dto;

import lombok.Data;

@Data
public class OCRJobDTO {
    private Long documentId;         // Eindeutige ID des Dokuments
    private String documentTitle;   // Titel des Dokuments
    private String fileUrl;         // URL des Dokuments in MinIO
    private int pageCount;          // Anzahl der Seiten im Dokument
}
