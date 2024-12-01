package com.dms.config;

import org.springframework.amqp.core.*;
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
}

