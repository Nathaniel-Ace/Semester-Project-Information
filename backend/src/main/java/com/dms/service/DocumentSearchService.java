package com.dms.service;

import com.dms.service.dto.DocumentSearchDTO;
import lombok.RequiredArgsConstructor;
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
public class DocumentSearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public List<DocumentSearchDTO> findAllDocuments() {
        // Elasticsearch Query, um alle Dokumente zu finden
        Query query = new StringQuery("{ \"match_all\": {} }");
        SearchHits<DocumentSearchDTO> searchHits = elasticsearchTemplate.search(query, DocumentSearchDTO.class);

        // Konvertiere Ergebnisse in eine Liste
        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    public DocumentSearchDTO findDocumentById(Long id) {
        // Elasticsearch Query nach Dokument-ID
        Query query = new StringQuery(String.format("{ \"term\": { \"documentId\": %d } }", id));
        SearchHits<DocumentSearchDTO> searchHits = elasticsearchTemplate.search(query, DocumentSearchDTO.class);

        // Falls ein Treffer vorhanden ist, gib das Dokument zurück
        return searchHits.stream()
                .map(SearchHit::getContent)
                .findFirst()
                .orElse(null);
    }

    public List<DocumentSearchDTO> searchDocuments(String query) {
        // Elasticsearch Full-Text Query
        Query searchQuery = new StringQuery(String.format("{ \"multi_match\": { \"query\": \"%s\", \"fields\": [\"title\", \"ocrText\"] } }", query));
        SearchHits<DocumentSearchDTO> searchHits = elasticsearchTemplate.search(searchQuery, DocumentSearchDTO.class);

        // Konvertiere Ergebnisse in eine Liste
        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
