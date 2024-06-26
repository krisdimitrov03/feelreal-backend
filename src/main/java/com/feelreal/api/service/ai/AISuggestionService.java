package com.feelreal.api.service.ai;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.model.Article;
import com.feelreal.api.model.Event;
import com.feelreal.api.model.Tip;

import java.util.List;
import java.util.UUID;

public interface AISuggestionService {

    OperationResult<Article> recommendPersonalizedArticle(UUID userId);

    OperationResult<Event> recommendPersonalizedEvent(UUID userId);

    OperationResult<List<Tip>> recommendPersonalizedTips(UUID userId);

}
