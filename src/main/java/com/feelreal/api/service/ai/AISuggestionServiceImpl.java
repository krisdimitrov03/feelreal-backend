package com.feelreal.api.service.ai;

import com.feelreal.api.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class AISuggestionServiceImpl implements AISuggestionService {
    @Value("${gpt.api.key}")
    private String apiKey;

    @Value("${gpt.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public AISuggestionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String recommendPersonalizedActivity(String username) {
        return callGpt(
                "Give me a personalized activity for Product manager with mood value 4 out of 10");
    }

    @Override
    public String provideInsights(User user) {
        return null;
    }

    @Override
    public String analyzeEmotion(String userText) {
        return null;
    }

    @Override
    public String suggestBehaviorChange(String userActivityLog) {
        return null;
    }

    private String callGpt(String messageText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", messageText);
        messages.put(message);

        JSONObject body = new JSONObject();
        body.put("messages", messages);
        body.put("model", "gpt-3.5-turbo");
        body.put("max_tokens", 15);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
        return response.getBody();
    }
}
