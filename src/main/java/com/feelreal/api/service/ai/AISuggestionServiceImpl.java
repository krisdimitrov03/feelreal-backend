package com.feelreal.api.service.ai;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.common.ResultStatus;
import com.feelreal.api.dto.event.EventCreateRequest;
import com.feelreal.api.dto.wellnesschecks.WellnessCheckResponse;
import com.feelreal.api.model.Article;
import com.feelreal.api.model.Event;
import com.feelreal.api.model.User;
import com.feelreal.api.model.enumeration.MoodType;
import com.feelreal.api.service.article.ArticleService;
import com.feelreal.api.service.authentication.UserService;
import com.feelreal.api.service.wellnessChecks.WellnessCheckService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AISuggestionServiceImpl implements AISuggestionService {
    @Value("${gpt.api.key}")
    private String apiKey;

    @Value("${gpt.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final WellnessCheckService wellnessCheckService;
    private final UserService userService;
    private final ArticleService articleService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AISuggestionServiceImpl(RestTemplate restTemplate, WellnessCheckService wellnessCheckService, UserService userService, ArticleService articleService, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.wellnessCheckService = wellnessCheckService;
        this.userService = userService;
        this.articleService = articleService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Article recommendPersonalizedActivity(UUID uuid) {
        Optional<User> userOptional = getUser(uuid);
        if (userOptional.isEmpty()) {
            return null;
        }

        List<WellnessCheckResponse> wellnessChecks = getWellnessChecks(userOptional.get().getId());
        if (wellnessChecks == null) {
            return null;
        }

        JSONArray wellnessCheckArray = formatWellnessChecks(wellnessChecks);
        String recommendationMessage = createRecommendationMessage(wellnessCheckArray);

        String gptResponse = callGpt(recommendationMessage);
        int articleTypeInt = extractArticleTypeFromGptResponse(gptResponse);
        MoodType articleType = MoodType.values()[articleTypeInt];

        List<Article> articles = getArticlesByType(articleType);
        if (articles == null || articles.isEmpty()) {
            return null;
        }

        return selectRandomArticle(articles);
    }

    private Optional<User> getUser(UUID uuid) {
        return userService.getById(uuid);
    }

    private List<WellnessCheckResponse> getWellnessChecks(UUID userId) {
        OperationResult<List<WellnessCheckResponse>> result = wellnessCheckService.getForUser(userId);
        if (result.getStatus() != ResultStatus.SUCCESS) {
            return null;
        }
        return result.getData();
    }

    private JSONArray formatWellnessChecks(List<WellnessCheckResponse> wellnessChecks) {
        JSONArray array = new JSONArray();
        for (WellnessCheckResponse check : wellnessChecks) {
            JSONObject checkObject = new JSONObject();
            checkObject.put("date", check.getDate());
            String type = (check.getType() == 0) ? "How are you feeling today out of ten" : "Compare to yesterday";
            String valueDescription = (check.getType() == 0) ? String.valueOf(check.getValue()) :
                    (check.getValue() == 11 ? "better than yesterday" : "worse than yesterday");
            checkObject.put("type", type);
            checkObject.put("value", valueDescription);
            array.put(checkObject);
        }
        return array;
    }

    private String createEventRecommendationMessages(JSONArray wellnessCheckArray) {
        return String.format("""
interface WellnessCheck {
    date: Date;
    type: "How are you feeling today out of ten" | "Compare to yesterday";
    value: number; // 1 to 10 for "How are you feeling today" and "better than yesterday" or "worse than yesterday" for "Compare to yesterday"
};

These are user wellness checks:
%s

Based on the data from above, i want you to create a json object that has the following fields - title, notes, dateTimeStart, dateTimeEnd (both of which should be in the format yyyy-MM-dd HH:mm:ss) and repeatMode where repeatMode is 0 for once, 1 for daily, 2 for weekly, 3 for monthly, the default is 4.
The title should be short and tongue-in-cheek. The notes field should be no more than one sentence. Directly return the event itself, no fluff. And the event should be tailor-made for the specific user. The event should be some kind of social event such as a concert or something. You should be all means return a json object, nothing in front of the object and after it.
""", wellnessCheckArray.toString());
    }

    private String createRecommendationMessage(JSONArray wellnessCheckArray) {
        return String.format("""
interface WellnessCheck {
    date: Date;
    type: "How are you feeling today out of ten" | "Compare to yesterday";
    value: number; // 1 to 10 for "How are you feeling today" and "better than yesterday" or "worse than yesterday" for "Compare to yesterday"
};

These are user wellness checks:
%s

Suggest the category for the mood of the user based on the wellness checks. The categories are:
"constant_happiness", "constant_sadness", "was_happy_now_sad", "was_sad_now_happy".

Return one of the categories above. Start with your answer first, then explain it.
""", wellnessCheckArray.toString());
    }

    private List<Article> getArticlesByType(MoodType articleType) {
        OperationResult<List<Article>> result = articleService.getByType(articleType);
        if (result.getStatus() != ResultStatus.SUCCESS) {
            return null;
        }
        return result.getData();
    }

    private Article selectRandomArticle(List<Article> articles) {
        return articles.get((int) (Math.random() * articles.size()));
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

    @Override
    public EventCreateRequest recommendPersonalizedEvent(UUID uuid) {
        Optional<User> userOptional = getUser(uuid);
        if (userOptional.isEmpty()) {
            return null;
        }

        List<WellnessCheckResponse> wellnessChecks = getWellnessChecks(userOptional.get().getId());
        if (wellnessChecks == null) {
            return null;
        }

        JSONArray wellnessCheckArray = formatWellnessChecks(wellnessChecks);
        String recommendationMessage = createEventRecommendationMessages(wellnessCheckArray);

        String gptResponse = callGpt(recommendationMessage);

        return convertResponseToEventCreateRequest(gptResponse);
    }

    private EventCreateRequest convertResponseToEventCreateRequest(String gptResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Clean the input string if it starts with a non-JSON structure
        String cleanedResponse = cleanInputString(gptResponse);

        try {
            JsonNode eventNode = objectMapper.readTree(cleanedResponse);

            String title = eventNode.get("title").asText();
            String notes = eventNode.get("notes").asText();
            String dateTimeStart = eventNode.get("dateTimeStart").asText();
            String dateTimeEnd = eventNode.get("dateTimeEnd").asText();
            int repeatMode = eventNode.get("repeatMode").asInt();

            return new EventCreateRequest(title, notes, dateTimeStart, dateTimeEnd, repeatMode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String cleanInputString(String input) {
        // Check if the input starts with the JSON object
        int jsonStartIndex = input.indexOf("{");
        if (jsonStartIndex != -1) {
            return input.substring(jsonStartIndex);
        }
        return input;
    }

    public Event parseEvent(String jsonString) throws Exception {
        JsonNode rootNode = objectMapper.readTree(jsonString);

        if (rootNode.has("title") && rootNode.has("notes") && rootNode.has("dateTimeStart") && rootNode.has("dateTimeEnd") && rootNode.has("repeatMode")) {
            // JSON is in expected format
            return objectMapper.treeToValue(rootNode, Event.class);
        } else {
            // Handle other formats or throw an exception
            throw new IllegalArgumentException("Invalid JSON format");
        }
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
        body.put("model", "gpt-4o");
        body.put("max_tokens", 100);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

        JSONObject responseBody = new JSONObject(response.getBody());

        JSONArray choices = responseBody.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject messageObject = firstChoice.getJSONObject("message");
        return messageObject.getString("content");
    }

    private int extractArticleTypeFromGptResponse(String gptResponse) {
        System.out.println(Arrays.toString(gptResponse.split("\"")));
        try {
            String category = gptResponse.split("\"")[1];
            return switch (category) {
                case "constant_happiness" -> 0;
                case "was_happy_now_sad" -> 1;
                case "was_sad_now_happy" -> 2;
                case "constant_sadness" -> 3;
                default -> 4;
            };
        } catch (Exception e) {
            return 4;
        }
    }
}