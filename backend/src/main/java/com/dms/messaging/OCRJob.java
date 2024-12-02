package com.dms.messaging;

import lombok.Data;
import java.io.Serializable;

@Data
public class OCRJob implements Serializable {
    private Long documentId;
    private String documentTitle;
    private String fileUrl;
    private String metadata;
    private int pageCount;
}
