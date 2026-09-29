package com.portfolio.ai.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class VectorStoreServiceImpl implements VectorStoreService {
    private final VectorStore vectorStore;

    /**
     * Load initial documents into the ChromaDB vector database.
     */
    @Override
    public void addSampleDocuments() {
        List<Document> documents = List.of(
                new Document("The Smart Agent project uses Spring Boot 3.3.5, Java 21, and Spring AI to build robust microservice architectures integrated with local AI."),
                new Document("ChromaDB is an open-source vector database optimized for storing embeddings and performing similarity searches for RAG systems."),
                new Document("Ollama allows running local language models like Phi-3 and embedding models like nomic-embed-text natively, ensuring high speed and complete data privacy.")
        );

        log.info("Adding {} sample documents to ChromaDB vector store...", documents.size());
        vectorStore.add(documents);
        log.info("Sample documents successfully indexed into ChromaDB.");
    }
}
