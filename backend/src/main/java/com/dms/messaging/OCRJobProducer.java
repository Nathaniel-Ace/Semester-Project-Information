package com.dms.messaging;

import com.dms.service.dto.OCRJobDTO;
import com.dms.service.mapper.OCRJobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OCRJobProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOCRJob(String queueName, OCRJobDTO ocrJobDTO) {
        OCRJob ocrJob = OCRJobMapper.toEntity(ocrJobDTO);
        rabbitTemplate.convertAndSend(queueName, ocrJob);
        System.out.println("OCR-Job gesendet: " + ocrJob);
    }
}
