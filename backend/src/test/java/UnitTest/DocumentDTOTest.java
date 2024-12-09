package UnitTest;

import com.dms.service.dto.DocumentDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentDTOTest {

    @Test
    void testAllArgsConstructor() {
        // Using all-args constructor
        DocumentDTO document = new DocumentDTO(1L, "Test Title", "Test Content", "http://example.com/file.pdf", 10);

        assertEquals(1L, document.getId());
        assertEquals("Test Title", document.getTitle());
        assertEquals("Test Content", document.getContent());
        assertEquals("http://example.com/file.pdf", document.getFileUrl());
        assertEquals(10, document.getPageCount());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        // Using no-args constructor and setters
        DocumentDTO document = new DocumentDTO();
        document.setId(2L);
        document.setTitle("Another Title");
        document.setContent("Another Content");
        document.setFileUrl("http://example.com/anotherfile.pdf");
        document.setPageCount(5);

        assertEquals(2L, document.getId());
        assertEquals("Another Title", document.getTitle());
        assertEquals("Another Content", document.getContent());
        assertEquals("http://example.com/anotherfile.pdf", document.getFileUrl());
        assertEquals(5, document.getPageCount());
    }

    @Test
    void testBuilder() {
        // Using builder pattern
        DocumentDTO document = DocumentDTO.builder()
                .id(3L)
                .title("Built Title")
                .content("Built Content")
                .fileUrl("http://example.com/builtfile.pdf")
                .pageCount(15)
                .build();

        assertEquals(3L, document.getId());
        assertEquals("Built Title", document.getTitle());
        assertEquals("Built Content", document.getContent());
        assertEquals("http://example.com/builtfile.pdf", document.getFileUrl());
        assertEquals(15, document.getPageCount());
    }

    @Test
    void testEqualsAndHashCode() {
        // Test equality and hashCode
        DocumentDTO document1 = new DocumentDTO(4L, "Title", "Content", "http://example.com/file.pdf", 10);
        DocumentDTO document2 = new DocumentDTO(4L, "Title", "Content", "http://example.com/file.pdf", 10);
        DocumentDTO document3 = new DocumentDTO(5L, "Different", "Other", "http://example.com/otherfile.pdf", 8);

        assertEquals(document1, document2); // Objects with same data should be equal
        assertNotEquals(document1, document3); // Objects with different data should not be equal
        assertEquals(document1.hashCode(), document2.hashCode()); // Equal objects should have same hashCode
    }
}
