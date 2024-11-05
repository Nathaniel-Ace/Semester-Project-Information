package com.dms.persistence.mapper;

import com.dms.persistence.dto.DocumentDTO;
import com.dms.persistence.entity.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDTO toDTO(Document document);
    Document toEntity(DocumentDTO documentDTO);
}

