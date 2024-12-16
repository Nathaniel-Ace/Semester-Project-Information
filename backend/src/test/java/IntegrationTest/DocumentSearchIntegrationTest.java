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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = DmsApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DocumentSearchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentSearchService documentSearchService;

    @BeforeEach
    public void setUp() {
        DocumentSearchDTO document1 = new DocumentSearchDTO();
        document1.setId(1L);
        document1.setTitle("Test Document 1");
        document1.setOcrText("This is the first test document content");

        DocumentSearchDTO document2 = new DocumentSearchDTO();
        document2.setId(2L);
        document2.setTitle("Test Document 2");
        document2.setOcrText("This is the second test document content");

        List<DocumentSearchDTO> documents = Arrays.asList(document1, document2);

        when(documentSearchService.searchDocuments(anyString())).thenReturn(documents);
    }

    @Test
    public void testSearchDocuments() throws Exception {
        mockMvc.perform(get("/api/v1/documents/search?query=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Document 1"))
                .andExpect(jsonPath("$[0].ocrText").value("This is the first test document content"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Test Document 2"))
                .andExpect(jsonPath("$[1].ocrText").value("This is the second test document content"));
    }
}