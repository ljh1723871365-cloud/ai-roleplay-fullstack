package com.example.airoleplay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * API Controller that handles all incoming HTTP requests for the application.
 */
@RestController
public class ChatController {

    // Inject the API key from the application.properties file.
    @Value("${google.api.key}")
    private String geminiApiKey;

    // Create a reusable HttpClient instance.
    private final HttpClient httpClient = HttpClient.newHttpClient();
    // Create a reusable ObjectMapper for JSON processing.
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handles the POST /api/chat request from the frontend.
     * @param chatRequest The request body containing the system prompt and conversation history.
     * @return A response entity containing the AI's reply.
     */
    @PostMapping(value = "/api/chat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatResponse> handleChat(@RequestBody ChatRequest chatRequest) {
        try {
            // 1. Build the request body for the Google Gemini API.
            String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-05-20:generateContent?key=" + geminiApiKey;
            GeminiRequest geminiPayload = new GeminiRequest(
                    chatRequest.conversationHistory(),
                    new SystemInstruction(List.of(new Part(chatRequest.systemPrompt())))
            );

            // 2. Create the HttpRequest object.
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(geminiApiUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(geminiPayload)))
                    .build();

            // 3. Send the request and receive the response.
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Check the response status code.
            if (response.statusCode() != 200) {
                System.err.println("Google API Error: " + response.body());
                return ResponseEntity.status(response.statusCode()).body(new ChatResponse("Error from Google API. Check server logs for details."));
            }

            // 5. Parse the response from the Google API and extract the AI's text reply.
            GeminiResponse geminiResponse = objectMapper.readValue(response.body(), GeminiResponse.class);
            String aiText = geminiResponse.candidates().get(0).content().parts().get(0).text();

            // 6. Wrap the AI's reply and send it back to the frontend.
            return ResponseEntity.ok(new ChatResponse(aiText));

        } catch (Exception e) {
            // A general exception handler for any issues during the process.
            System.err.println("Internal Server Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ChatResponse("An internal server error occurred. Check server logs for details."));
        }
    }

    // --- Data Transfer Objects (DTOs) using Java 17 Records for conciseness ---

    // Structure for the request body sent from the frontend to this server.
    record ChatRequest(String systemPrompt, List<Content> conversationHistory) {}

    // Structure for the response body sent from this server back to the frontend.
    record ChatResponse(String aiResponse) {}

    // --- Data structures matching the Google Gemini API's JSON format ---

    // Structure for the request body sent to the Gemini API.
    record GeminiRequest(List<Content> contents, SystemInstruction systemInstruction) {}

    record SystemInstruction(@JsonProperty("parts") List<Part> parts) {}

    // Structure for the response body received from the Gemini API.
    @JsonIgnoreProperties(ignoreUnknown = true)
    record GeminiResponse(List<Candidate> candidates) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Candidate(Content content) {}

    record Content(String role, List<Part> parts) {}

    record Part(String text) {}
}

