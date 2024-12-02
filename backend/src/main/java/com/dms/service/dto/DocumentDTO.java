package com.dms.service.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DocumentDTO {
    private Long id;              // Unique identifier for the document
    private String title;          // Document title or name
    private String description;   // Optional description of the document
    private String fileUrl;       // URL of the uploaded file in the cloud
    private int pageCount;        // Number of pages in the document
}
