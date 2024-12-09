
package UnitTest;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.dms.config.ElasticsearchConfig;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ElasticsearchConfigTest {

    @InjectMocks
    private ElasticsearchConfig elasticsearchConfig;

    @Mock
    private RestClient mockRestClient;

    @Mock
    private RestClientTransport mockRestClientTransport;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(elasticsearchConfig, "elasticsearchHost", "localhost");
        ReflectionTestUtils.setField(elasticsearchConfig, "elasticsearchPort", 9200);
    }

    @Test
    public void testRestClient() {
        RestClient restClient = elasticsearchConfig.restClient();
        assertNotNull(restClient, "RestClient should not be null");
    }



}
