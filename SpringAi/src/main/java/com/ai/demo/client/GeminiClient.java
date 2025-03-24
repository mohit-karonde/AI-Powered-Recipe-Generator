package com.ai.demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class GeminiClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create();

    public Mono<String> generateRecipe(String ingredients, String cuisine) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
        String requestBody = """
            {
                "contents": [{
                    "parts": [{
                        "text": "Create a %s recipe using: %s"
                    }]
                }]
            }
            """.formatted(cuisine, ingredients);

        return webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.err.println("API Error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
                    return Mono.error(new RuntimeException("Failed to call Gemini API"));
                });
    }
}
