package com.dms.service.mapper;

import com.dms.service.dto.DocumentDTO;
import com.dms.persistence.entity.Document;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    // Mapping von Document zu DocumentDTO
    DocumentDTO toDTO(Document document);

    // Mapping von DocumentDTO zu Document
    Document toEntity(DocumentDTO documentDTO);

    // Mapping einer Liste von Documents zu einer Liste von DocumentDTOs
    Iterable<DocumentDTO> toDTOList(List<Document> all);


}
