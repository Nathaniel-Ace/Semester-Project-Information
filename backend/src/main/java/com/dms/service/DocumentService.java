package com.dms.service;

import com.dms.persistence.repo.DocumentRepo;
import com.dms.service.mapper.DocumentMapper;
import com.dms.service.dto.DocumentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentMapper documentMapper;
    private final DocumentRepo documentRepo;

    public DocumentDTO saveDocument(DocumentDTO documentDTO) {
        //before saving check if document is valid (e.g. title is not null, right format, etc.)
        return documentMapper.toDTO(documentRepo.save(documentMapper.toEntity(documentDTO)));
    }

    public Iterable<DocumentDTO> findAllDocuments() {
        return documentMapper.toDTOList(documentRepo.findAll());
    }

    public DocumentDTO findDocumentById(Long id) {
        return documentRepo.findById(id).map(documentMapper::toDTO).orElse(null);
    }

    public DocumentDTO findDocumentByName(String name) {
        return documentMapper.toDTO(documentRepo.findByTitle(name));
    }

    public DocumentDTO updateDocument(Long id, DocumentDTO updatedDocument) {
        return documentRepo.findById(id).map(document -> {
            document.setTitle(updatedDocument.getTitle());
            document.setContent(updatedDocument.getContent());
            return documentMapper.toDTO(documentRepo.save(document));
        }).orElse(null);
    }

    public void deleteDocumentById(Long id) {
        // Check if the document exists
        if (documentRepo.existsById(id)) {
            documentRepo.deleteById(id);
        } else {
            throw new IllegalArgumentException("Document with ID " + id + " does not exist.");
        }
    }
}
