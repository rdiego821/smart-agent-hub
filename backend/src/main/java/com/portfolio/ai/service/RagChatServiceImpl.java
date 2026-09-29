package com.portfolio.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagChatServiceImpl implements RagChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagChatServiceImpl(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        // We configured a base system prompt specialized for RAG.
        this.chatClient = chatClientBuilder
                .defaultSystem("You are an expert technical assistant. Answer the user's questions based exclusively on the provided context. " +
                        "If the answer is not in the context, politely state that you do not have that information.")
                .build();
        this.vectorStore = vectorStore;
    }

    @Override
    public String chatWithRag(String userMessage) {
        // 1. Build modern search request using Spring AI Builder pattern
        SearchRequest searchRequest = SearchRequest.builder()
                .query(userMessage)
                .topK(3)
                .build();

        List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest);

        // 2. Extract and concatenate the content of the retrieved context.
        String context = "";
        if (similarDocuments != null && !similarDocuments.isEmpty()) {
            context = similarDocuments.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n\n"));
        }

        // 3. Construct prompt enriched with retrieved RAG context
        String promptWithContext = String.format(
                "Contexto recuperado:%n%s%n%nPregunta del usuario: %s",
                context,
                userMessage
        );

        // 4. Send query to local LLM via Ollama
        return chatClient.prompt()
                .user(promptWithContext)
                .call()
                .content();
    }
}
