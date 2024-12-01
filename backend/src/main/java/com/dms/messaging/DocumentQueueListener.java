package com.dms.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentQueueListener {

    @RabbitListener(queues = "documentQueue")
    public void handleMessage(String message) {
        System.out.println("Empfangene Nachricht: " + message);
        // Hier erfolgt die weitere Verarbeitung der Nachricht
    }
}
