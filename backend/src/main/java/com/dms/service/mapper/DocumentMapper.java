package com.dms.service.mapper;

import com.dms.persistence.dto.DocumentDTO;
import com.dms.persistence.entity.Document;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDTO toDTO(Document document);
    Document toEntity(DocumentDTO documentDTO);

    Iterable<com.dms.persistence.dto.DocumentDTO> toDTOList(List<Document> all);
}

