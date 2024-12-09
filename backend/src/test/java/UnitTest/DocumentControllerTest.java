package UnitTest;

import com.dms.api.DocumentController;
import com.dms.exception.DocumentStorageException;
import com.dms.service.DocumentSearchService;
import com.dms.service.DocumentService;
import com.dms.service.ElasticsearchTestService;
import com.dms.service.dto.DocumentDTO;
import com.dms.service.dto.DocumentSearchDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @Mock
    private DocumentSearchService documentSearchService;

    @Mock
    private ElasticsearchTestService elasticsearchTestService;

    @InjectMocks
    private DocumentController documentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveDocument() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setTitle("Test Title");
        documentDTO.setPageCount(10);

        when(documentService.saveDocument(any(DocumentDTO.class), eq(file))).thenReturn(documentDTO);

        ResponseEntity<DocumentDTO> response = documentController.saveDocument(file, "Test Title", 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Title", response.getBody().getTitle());
        assertEquals(10, response.getBody().getPageCount());
    }

    @Test
    public void testFindAllDocuments() {
        DocumentSearchDTO documentSearchDTO = new DocumentSearchDTO();
        when(documentSearchService.findAllDocuments()).thenReturn(Collections.singletonList(documentSearchDTO));

        ResponseEntity<List<DocumentSearchDTO>> response = documentController.findAllDocuments();

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testFindDocumentById() {
        DocumentSearchDTO documentSearchDTO = new DocumentSearchDTO();
        when(documentSearchService.findDocumentById(1L)).thenReturn(documentSearchDTO);

        ResponseEntity<DocumentSearchDTO> response = documentController.findDocumentById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testSearchDocuments() {
        DocumentSearchDTO documentSearchDTO = new DocumentSearchDTO();
        when(documentSearchService.searchDocuments("query")).thenReturn(Collections.singletonList(documentSearchDTO));

        ResponseEntity<List<DocumentSearchDTO>> response = documentController.searchDocuments("query");

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testUpdateDocument() {
        DocumentDTO documentDTO = new DocumentDTO();
        when(documentService.updateDocument(eq(1L), any(DocumentDTO.class))).thenReturn(documentDTO);

        ResponseEntity<DocumentDTO> response = documentController.updateDocument(1L, documentDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testDeleteDocumentById() {
        doNothing().when(documentService).deleteDocumentById(1L);

        ResponseEntity<Void> response = documentController.deleteDocumentById(1L);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void testTestElasticsearchConnection() {
        when(elasticsearchTestService.testConnection()).thenReturn("Connection successful");

        ResponseEntity<String> response = documentController.testElasticsearchConnection();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Connection successful", response.getBody());
    }
}


