package com.dms.messaging;

import com.dms.service.dto.OCRJobDTO;
import com.dms.service.mapper.OCRJobMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OCRJobListener {

    @RabbitListener(queues = "OCR_QUEUE")
    public void receiveOCRJob(OCRJob ocrJob) {
        OCRJobDTO ocrJobDTO = OCRJobMapper.toDTO(ocrJob);
        System.out.println("OCR-Job empfangen: " + ocrJobDTO);
        // Weiterverarbeitung des OCR-Jobs mit DTO
    }
}
