package com.dms.persistence;

import com.dms.persistence.dto.DocumentDTO;
import com.dms.persistence.entity.Document;
import com.dms.persistence.mapper.DocumentMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class DocumentMapperTest {

    @Autowired
    private DocumentMapper documentMapper;

    @Test
    public void testMapping() {
        Document document = new Document();
        document.setTitle("Test");
        DocumentDTO documentDTO = documentMapper.toDTO(document);

        assertEquals(document.getTitle(), documentDTO.getTitle());
    }
}
