package com.dms.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue documentQueue() {
        System.out.println("Queue 'documentQueue' wird erstellt.");
        return new Queue("documentQueue", false);
    }

    @Bean
    public DirectExchange directExchange() {
        System.out.println("Exchange 'documentExchange' wird erstellt.");
        return new DirectExchange("documentExchange");
    }

    @Bean
    public Binding binding(Queue documentQueue, DirectExchange directExchange) {
        System.out.println("Binding für 'documentQueue' und 'documentExchange' wird erstellt.");
        return BindingBuilder.bind(documentQueue).to(directExchange).with("documentRoutingKey");
    }

    // Define OCR_QUEUE
    @Bean
    public Queue ocrQueue() {
        System.out.println("Queue 'OCR_QUEUE' wird erstellt.");
        return new Queue("OCR_QUEUE", true); // Durable queue
    }

    // Define RESULT_QUEUE
    @Bean
    public Queue resultQueue() {
        System.out.println("Queue 'RESULT_QUEUE' wird erstellt.");
        return new Queue("RESULT_QUEUE", true); // Durable queue
    }

    // Define a JSON message converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Set the message converter in the RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}
