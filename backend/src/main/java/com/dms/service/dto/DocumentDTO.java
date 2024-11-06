package com.dms.service.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DocumentDTO {
    private Long id;
    private String title;
    private String author;
    private String content;

    // Getter und Setter
}

