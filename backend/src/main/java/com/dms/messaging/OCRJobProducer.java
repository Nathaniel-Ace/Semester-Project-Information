package com.dms.messaging;

import com.dms.service.dto.OCRJobDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OCRJobProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOCRJob(String queueName, OCRJobDTO ocrJobDTO) {
        try {
            rabbitTemplate.convertAndSend(queueName, ocrJobDTO);
            System.out.println("OCR-Job gesendet: " + new ObjectMapper().writeValueAsString(ocrJobDTO));
        } catch (Exception e) {
            System.err.println("Fehler beim Senden der Nachricht: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
