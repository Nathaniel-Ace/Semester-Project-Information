package UnitTest;

import com.dms.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class RabbitMQConfigTest {

    private final RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();

    @Test
    public void testDocumentQueue() {
        Queue queue = rabbitMQConfig.documentQueue();
        assertNotNull(queue);
        assertEquals("documentQueue", queue.getName());
        assertFalse(queue.isDurable());
    }

    @Test
    public void testDirectExchange() {
        DirectExchange exchange = rabbitMQConfig.directExchange();
        assertNotNull(exchange);
        assertEquals("documentExchange", exchange.getName());
    }

    @Test
    public void testBinding() {
        Queue queue = rabbitMQConfig.documentQueue();
        DirectExchange exchange = rabbitMQConfig.directExchange();
        Binding binding = rabbitMQConfig.binding(queue, exchange);
        assertNotNull(binding);
        assertEquals("documentQueue", binding.getDestination());
        assertEquals("documentRoutingKey", binding.getRoutingKey());
        assertEquals("documentExchange", binding.getExchange());
    }

    @Test
    public void testOcrQueue() {
        Queue queue = rabbitMQConfig.ocrQueue();
        assertNotNull(queue);
        assertEquals("OCR_QUEUE", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    public void testResultQueue() {
        Queue queue = rabbitMQConfig.resultQueue();
        assertNotNull(queue);
        assertEquals("RESULT_QUEUE", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    public void testJsonMessageConverter() {
        MessageConverter converter = rabbitMQConfig.jsonMessageConverter();
        assertNotNull(converter);
        assertTrue(converter instanceof Jackson2JsonMessageConverter);
    }

    @Test
    public void testRabbitTemplate() {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        MessageConverter converter = rabbitMQConfig.jsonMessageConverter();
        RabbitTemplate rabbitTemplate = rabbitMQConfig.rabbitTemplate(connectionFactory, converter);
        assertNotNull(rabbitTemplate);
        assertEquals(converter, rabbitTemplate.getMessageConverter());
    }
}