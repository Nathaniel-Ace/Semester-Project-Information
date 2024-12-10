package IntegrationTest;

import com.dms.DmsApplication;
import com.dms.messaging.OCRJobProducer;
import com.dms.service.MinioService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = DmsApplication.class)
@ActiveProfiles("test")  // Aktiviert application-test.properties
@AutoConfigureMockMvc
public class DocumentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MinioService minioService;

    @MockBean
    private OCRJobProducer ocrJobProducer;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testUploadDocument() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_document.pdf",
                "application/pdf",
                "This is a test PDF content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/documents/upload")
                        .file(file)
                        .param("title", "Test Document")
                        .param("pageCount", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Document"))
                .andExpect(jsonPath("$.pageCount").value(3))
                .andExpect(jsonPath("$.fileUrl").isNotEmpty());
    }
}

