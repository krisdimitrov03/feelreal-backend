package com.feelreal.api.service.ai;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.common.ResultStatus;
import com.feelreal.api.dto.wellnesschecks.WellnessCheckResponse;
import com.feelreal.api.model.Article;
import com.feelreal.api.model.User;
import com.feelreal.api.model.enumeration.ArticleType;
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

    @Autowired
    public AISuggestionServiceImpl(RestTemplate restTemplate, WellnessCheckService wellnessCheckService, UserService userService, ArticleService articleService) {
        this.restTemplate = restTemplate;
        this.wellnessCheckService = wellnessCheckService;
        this.userService = userService;
        this.articleService = articleService;
    }

    @Override
    public Article recommendPersonalizedActivity(UUID uuid) {
        Optional<User> userOptional = userService.getById(uuid);
        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        UUID userId = user.getId();

        OperationResult<List<WellnessCheckResponse>> wellnessCheckResult = wellnessCheckService.getForUser(userId);

        if (wellnessCheckResult.getStatus() != ResultStatus.SUCCESS) {
            return null;
        }

        List<WellnessCheckResponse> wellnessChecks = wellnessCheckResult.getData();

        JSONArray wellnessCheckArray = new JSONArray();
        for (WellnessCheckResponse check : wellnessChecks) {
            JSONObject checkObject = new JSONObject();
            checkObject.put("date", check.getDate());

            String type;
            String valueDescription;

            if (check.getType() == 0) {
                type = "How are you feeling today out of ten";
                valueDescription = String.valueOf(check.getValue());
            } else {
                type = "Compare to yesterday";
                valueDescription = check.getValue() == 11 ? "better than yesterday" : "worse than yesterday";
            }

            checkObject.put("type", type);
            checkObject.put("value", valueDescription);
            wellnessCheckArray.put(checkObject);
        }


        String recommendationMessage = String.format("""
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

        int articleTypeInt = Integer.parseInt(callGpt(recommendationMessage));

        // get the articles for said type
        ArticleType articleType = ArticleType.values()[articleTypeInt];

        OperationResult<List<Article>> getArticlesResult = articleService.getByType(articleType);

        if(getArticlesResult.getStatus() != ResultStatus.SUCCESS) {
            return null;
        }
        List<Article> articles = getArticlesResult.getData();

        // select a random article from articles

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
        body.put("max_tokens", 100);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

        JSONObject responseBody = new JSONObject(response.getBody());

        JSONArray choices = responseBody.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject messageObject = firstChoice.getJSONObject("message");
        String recommendationMessage = messageObject.getString("content");

        assert recommendationMessage != null;

        int c = getArticleTypeNeeded(recommendationMessage);

        return Integer.toString(c);
    }

    private int getArticleTypeNeeded(String recommendationMessage) {
        System.out.println(Arrays.toString(recommendationMessage.split("\"")));
        try{
            String category = recommendationMessage.split("\"")[1];
            return switch (category) {
                case "constant_happiness" -> 0;
                case "was_happy_now_sad" -> 1;
                case "was_sad_now_happy" -> 2;
                case "constant_sadness" -> 3;
                default -> 4;
            };
        }
        catch (Exception e){
            return 4;
        }
    }
}
