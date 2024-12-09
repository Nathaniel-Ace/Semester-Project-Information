package UnitTest;

import com.dms.messaging.OCRJob;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OCRJobTest {

    @Test
    public void testGettersAndSetters() {
        OCRJob ocrJob = new OCRJob();
        ocrJob.setDocumentId(1L);
        ocrJob.setDocumentTitle("Test Document");
        ocrJob.setFileUrl("http://example.com/file.pdf");
        ocrJob.setMetadata("Test Metadata");
        ocrJob.setPageCount(10);

        assertEquals(1L, ocrJob.getDocumentId());
        assertEquals("Test Document", ocrJob.getDocumentTitle());
        assertEquals("http://example.com/file.pdf", ocrJob.getFileUrl());
        assertEquals("Test Metadata", ocrJob.getMetadata());
        assertEquals(10, ocrJob.getPageCount());
    }

    @Test
    public void testNoArgsConstructor() {
        OCRJob ocrJob = new OCRJob();
        assertNotNull(ocrJob);
    }

    @Test
    public void testToString() {
        OCRJob ocrJob = new OCRJob();
        ocrJob.setDocumentId(1L);
        ocrJob.setDocumentTitle("Test Document");
        ocrJob.setFileUrl("http://example.com/file.pdf");
        ocrJob.setMetadata("Test Metadata");
        ocrJob.setPageCount(10);

        String expected = "OCRJob(documentId=1, documentTitle=Test Document, fileUrl=http://example.com/file.pdf, metadata=Test Metadata, pageCount=10)";
        assertEquals(expected, ocrJob.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        OCRJob ocrJob1 = new OCRJob();
        ocrJob1.setDocumentId(1L);
        ocrJob1.setDocumentTitle("Test Document");
        ocrJob1.setFileUrl("http://example.com/file.pdf");
        ocrJob1.setMetadata("Test Metadata");
        ocrJob1.setPageCount(10);

        OCRJob ocrJob2 = new OCRJob();
        ocrJob2.setDocumentId(1L);
        ocrJob2.setDocumentTitle("Test Document");
        ocrJob2.setFileUrl("http://example.com/file.pdf");
        ocrJob2.setMetadata("Test Metadata");
        ocrJob2.setPageCount(10);

        assertEquals(ocrJob1, ocrJob2);
        assertEquals(ocrJob1.hashCode(), ocrJob2.hashCode());
    }

    @Test
    public void testNotEquals() {
        OCRJob ocrJob1 = new OCRJob();
        ocrJob1.setDocumentId(1L);
        ocrJob1.setDocumentTitle("Test Document");
        ocrJob1.setFileUrl("http://example.com/file.pdf");
        ocrJob1.setMetadata("Test Metadata");
        ocrJob1.setPageCount(10);

        OCRJob ocrJob2 = new OCRJob();
        ocrJob2.setDocumentId(2L);
        ocrJob2.setDocumentTitle("Another Document");
        ocrJob2.setFileUrl("http://example.com/anotherfile.pdf");
        ocrJob2.setMetadata("Another Metadata");
        ocrJob2.setPageCount(20);

        assertNotEquals(ocrJob1, ocrJob2);
    }
}

