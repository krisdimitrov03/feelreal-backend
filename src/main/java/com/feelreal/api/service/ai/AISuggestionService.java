package com.feelreal.api.service.ai;

import com.feelreal.api.model.Article;
import com.feelreal.api.model.User;

import java.util.UUID;

public interface AISuggestionService {

    Article recommendPersonalizedActivity(UUID uuid);

    String provideInsights(User user);

    String analyzeEmotion(String userText);

    String suggestBehaviorChange(String userActivityLog);

}
