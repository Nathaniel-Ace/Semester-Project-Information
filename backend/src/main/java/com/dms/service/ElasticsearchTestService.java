package com.dms.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.InfoResponse;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchTestService {
    private final ElasticsearchClient elasticsearchClient;

    public ElasticsearchTestService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public String testConnection() {
        try {
            InfoResponse info = elasticsearchClient.info();
            return "Connected to Elasticsearch: " + info.version().number();
        } catch (Exception e) {
            return "Failed to connect to Elasticsearch: " + e.getMessage();
        }
    }
}

