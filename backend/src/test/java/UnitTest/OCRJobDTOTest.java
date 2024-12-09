package UnitTest;

import com.dms.service.dto.OCRJobDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OCRJobDTOTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        // Using no-args constructor and setters
        OCRJobDTO ocrJob = new OCRJobDTO();
        ocrJob.setDocumentId(1L);
        ocrJob.setDocumentTitle("Sample Document");
        ocrJob.setFileUrl("http://example.com/document.pdf");
        ocrJob.setPageCount(5);

        assertEquals(1L, ocrJob.getDocumentId());
        assertEquals("Sample Document", ocrJob.getDocumentTitle());
        assertEquals("http://example.com/document.pdf", ocrJob.getFileUrl());
        assertEquals(5, ocrJob.getPageCount());
    }

    @Test
    void testAllArgsConstructor() {
        // Using all-args constructor (implicit due to @Data from Lombok)
        OCRJobDTO ocrJob = new OCRJobDTO();
        ocrJob.setDocumentId(2L);
        ocrJob.setDocumentTitle("Another Document");
        ocrJob.setFileUrl("http://example.com/anotherdocument.pdf");
        ocrJob.setPageCount(10);

        assertEquals(2L, ocrJob.getDocumentId());
        assertEquals("Another Document", ocrJob.getDocumentTitle());
        assertEquals("http://example.com/anotherdocument.pdf", ocrJob.getFileUrl());
        assertEquals(10, ocrJob.getPageCount());
    }

    @Test
    void testEqualsAndHashCode() {
        // Test equality and hashCode
        OCRJobDTO ocrJob1 = new OCRJobDTO();
        ocrJob1.setDocumentId(3L);
        ocrJob1.setDocumentTitle("Document 1");
        ocrJob1.setFileUrl("http://example.com/doc1.pdf");
        ocrJob1.setPageCount(3);

        OCRJobDTO ocrJob2 = new OCRJobDTO();
        ocrJob2.setDocumentId(3L);
        ocrJob2.setDocumentTitle("Document 1");
        ocrJob2.setFileUrl("http://example.com/doc1.pdf");
        ocrJob2.setPageCount(3);

        OCRJobDTO ocrJob3 = new OCRJobDTO();
        ocrJob3.setDocumentId(4L);
        ocrJob3.setDocumentTitle("Different Document");
        ocrJob3.setFileUrl("http://example.com/doc2.pdf");
        ocrJob3.setPageCount(5);

        assertEquals(ocrJob1, ocrJob2); // Objects with same data should be equal
        assertNotEquals(ocrJob1, ocrJob3); // Objects with different data should not be equal
        assertEquals(ocrJob1.hashCode(), ocrJob2.hashCode()); // Equal objects should have same hashCode
    }

    @Test
    void testToString() {
        OCRJobDTO ocrJob = new OCRJobDTO();
        ocrJob.setDocumentId(5L);
        ocrJob.setDocumentTitle("Test Document");
        ocrJob.setFileUrl("http://example.com/test.pdf");
        ocrJob.setPageCount(7);

        String expectedToString = "OCRJobDTO(documentId=5, documentTitle=Test Document, fileUrl=http://example.com/test.pdf, pageCount=7)";
        assertEquals(expectedToString, ocrJob.toString());
    }
}

