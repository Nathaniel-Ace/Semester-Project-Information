package UnitTest;

import com.dms.persistence.entity.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentTest {

    private Document document;

    @BeforeEach
    public void setUp() {
        document = new Document();
    }

    @Test
    public void testGettersAndSetters() {
        document.setId(1L);
        document.setTitle("Test Document");
        document.setContent("This is a test content.");
        document.setFileUrl("http://example.com/file.pdf");
        document.setPageCount(10);
        LocalDateTime now = LocalDateTime.now();
        document.setCreatedAt(now);
        document.setUpdatedAt(now);

        assertEquals(1L, document.getId());
        assertEquals("Test Document", document.getTitle());
        assertEquals("This is a test content.", document.getContent());
        assertEquals("http://example.com/file.pdf", document.getFileUrl());
        assertEquals(10, document.getPageCount());
        assertEquals(now, document.getCreatedAt());
        assertEquals(now, document.getUpdatedAt());
    }

    @Test
    public void testNoArgsConstructor() {
        Document doc = new Document();
        assertNotNull(doc);
    }

    @Test
    public void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Document doc = new Document(1L, "Test Document", "This is a test content.", "http://example.com/file.pdf", 10, now, now);
        assertEquals(1L, doc.getId());
        assertEquals("Test Document", doc.getTitle());
        assertEquals("This is a test content.", doc.getContent());
        assertEquals("http://example.com/file.pdf", doc.getFileUrl());
        assertEquals(10, doc.getPageCount());
        assertEquals(now, doc.getCreatedAt());
        assertEquals(now, doc.getUpdatedAt());
    }

    @Test
    public void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Document doc = Document.builder()
                .id(1L)
                .title("Test Document")
                .content("This is a test content.")
                .fileUrl("http://example.com/file.pdf")
                .pageCount(10)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, doc.getId());
        assertEquals("Test Document", doc.getTitle());
        assertEquals("This is a test content.", doc.getContent());
        assertEquals("http://example.com/file.pdf", doc.getFileUrl());
        assertEquals(10, doc.getPageCount());
        assertEquals(now, doc.getCreatedAt());
        assertEquals(now, doc.getUpdatedAt());
    }

    @Test
    public void testPrePersist() {
        document.prePersist();
        assertNotNull(document.getCreatedAt());
        assertNotNull(document.getUpdatedAt());
        assertEquals(document.getCreatedAt(), document.getUpdatedAt());
    }


}

