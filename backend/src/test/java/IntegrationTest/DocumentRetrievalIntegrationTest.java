package IntegrationTest;

import com.dms.DmsApplication;
import com.dms.service.DocumentSearchService;
import com.dms.service.dto.DocumentSearchDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = DmsApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DocumentRetrievalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentSearchService documentSearchService;

    @BeforeEach
    public void setUp() {
        DocumentSearchDTO document = new DocumentSearchDTO();
        document.setId(1L);
        document.setTitle("Test Document");
        document.setOcrText("This is a test document content");

        when(documentSearchService.findDocumentById(anyLong())).thenReturn(document);
    }

    @Test
    public void testFindDocumentById() throws Exception {
        mockMvc.perform(get("/api/v1/documents/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Document"))
                .andExpect(jsonPath("$.ocrText").value("This is a test document content"));
    }
}