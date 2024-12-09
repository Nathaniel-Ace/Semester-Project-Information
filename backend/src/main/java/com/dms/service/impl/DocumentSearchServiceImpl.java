package com.dms.service.impl;

import com.dms.service.DocumentSearchService;
import com.dms.service.dto.DocumentSearchDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentSearchServiceImpl implements DocumentSearchService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentSearchServiceImpl.class); // Logger hinzufügen
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<DocumentSearchDTO> findAllDocuments() {
        logger.info("Starting search for all documents."); // Log: Beginn der Suche nach allen Dokumenten
        Query query = new StringQuery("{ \"match_all\": {} }");
        SearchHits<DocumentSearchDTO> searchHits = elasticsearchTemplate.search(query, DocumentSearchDTO.class);

        logger.info("Found {} documents.", searchHits.getTotalHits()); // Log: Anzahl der gefundenen Dokumente

        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentSearchDTO findDocumentById(Long id) {
        logger.info("Searching for document with ID: {}", id); // Log: Beginn der Suche nach Dokument-ID
        Query query = new StringQuery(String.format("{ \"term\": { \"documentId\": %d } }", id));
        SearchHits<DocumentSearchDTO> searchHits = elasticsearchTemplate.search(query, DocumentSearchDTO.class);

        // Wenn das Dokument gefunden wird, gebe es zurück, andernfalls null
        DocumentSearchDTO result = searchHits.stream()
                .map(SearchHit::getContent)
                .findFirst()
                .orElse(null);

        if (result != null) {
            logger.info("Document with ID: {} found.", id); // Log: Dokument gefunden
        } else {
            logger.warn("Document with ID: {} not found.", id); // Log: Dokument nicht gefunden
        }

        return result;
    }

    @Override
    public List<DocumentSearchDTO> searchDocuments(String query) {
        logger.info("Performing search with query: {}", query); // Log: Beginn der Suche mit der übergebenen Query
        Query searchQuery = new StringQuery(String.format("{ \"multi_match\": { \"query\": \"%s\", \"fields\": [\"title\", \"ocrText\"] } }", query));
        SearchHits<DocumentSearchDTO> searchHits = elasticsearchTemplate.search(searchQuery, DocumentSearchDTO.class);

        logger.info("Found {} documents matching query: {}", searchHits.getTotalHits(), query); // Log: Anzahl der gefundenen Dokumente

        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
