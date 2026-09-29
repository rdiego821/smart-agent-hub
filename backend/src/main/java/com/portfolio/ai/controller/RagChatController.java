package com.portfolio.ai.controller;

import com.portfolio.ai.service.RagChatService;
import com.portfolio.ai.service.VectorStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/rag")
public class RagChatController {
    private final RagChatService ragChatService;
    private final VectorStoreService vectorStoreService;

    public RagChatController(RagChatService ragChatService, VectorStoreService vectorStoreService) {
        this.ragChatService = ragChatService;
        this.vectorStoreService = vectorStoreService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "The 'message' field is required."));
        }

        String response = ragChatService.chatWithRag(message);
        return ResponseEntity.ok(Map.of("response", response));
    }

    @PostMapping("/ingest")
    public ResponseEntity<Map<String, String>> ingestDocuments() {
        vectorStoreService.addSampleDocuments();
        return ResponseEntity.ok(Map.of("status", "Documents successfully indexed in ChromaDB."));
    }
}
