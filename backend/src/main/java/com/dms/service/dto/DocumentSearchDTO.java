package com.dms.service.dto;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(indexName = "documents") // Name des Indexes in Elasticsearch
public class DocumentSearchDTO {
    private Long id;                // Unique identifier for the document
    private String title;           // Document title or name
    private String ocrText;         // OCR-erfasster Text für die Suche
}
