package com.dms.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.port}")
    private int elasticsearchPort;

    @Bean
    public RestClient restClient() {
        // Erstellt einen RestClient für die Verbindung zu Elasticsearch
        return RestClient.builder(
                new HttpHost(elasticsearchHost, elasticsearchPort, "http")
        ).build();
    }

    @Bean
    public RestClientTransport restClientTransport(RestClient restClient) {
        // Transport-Schicht mit Jackson für die Serialisierung
        return new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClientTransport restClientTransport) {
        // ElasticsearchClient-Instanz basierend auf dem Transport
        return new ElasticsearchClient(restClientTransport);
    }
}
