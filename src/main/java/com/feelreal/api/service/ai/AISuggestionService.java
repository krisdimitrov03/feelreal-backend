package com.feelreal.api.service.ai;

import com.feelreal.api.model.User;

public interface AISuggestionService {

    String recommendPersonalizedActivity(String user);

    String provideInsights(User user);

    String analyzeEmotion(String userText);

    String suggestBehaviorChange(String userActivityLog);

}
