package com.dms.api;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue documentQueue() {
        return new Queue("documentQueue", false);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("documentExchange");
    }

    @Bean
    public Binding binding(Queue documentQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(documentQueue).to(directExchange).with("documentRoutingKey");
    }
}
