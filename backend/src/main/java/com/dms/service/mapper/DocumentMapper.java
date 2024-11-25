package com.dms.service.mapper;

import com.dms.service.dto.DocumentDTO;
import com.dms.persistence.entity.Document;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDTO toDTO(Document document);
    Document toEntity(DocumentDTO documentDTO);

    Iterable<DocumentDTO> toDTOList(List<Document> all);
}

