package com.feelreal.api.contoller;

import com.feelreal.api.config.jwt.UserPrincipal;
import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.event.EventCreateRequest;
import com.feelreal.api.model.Article;
import com.feelreal.api.model.Tip;
import com.feelreal.api.service.ai.AISuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final AISuggestionService aiSuggestionService;

    @Autowired
    public AIController(AISuggestionService aiSuggestionService) {
        this.aiSuggestionService = aiSuggestionService;
    }

    @GetMapping("/article")
    public ResponseEntity<Article> suggestArticle(@AuthenticationPrincipal UserPrincipal user) {
        OperationResult<Article> result = aiSuggestionService.recommendPersonalizedArticle(user.getId());

        return ResponseEntity.ok().body(result.getData());
    }

    @GetMapping("/tips")
    public ResponseEntity<List<Tip>> suggestTips(@AuthenticationPrincipal UserPrincipal user) {
        OperationResult<List<Tip>> result = aiSuggestionService.recommendPersonalizedTips(user.getId());

        return ResponseEntity.ok().body(result.getData());
    }

    @GetMapping("/event")
    public ResponseEntity<EventCreateRequest> suggestEvent(@AuthenticationPrincipal UserPrincipal user) {
        OperationResult<EventCreateRequest> result = aiSuggestionService.recommendPersonalizedEvent(user.getId());

        return ResponseEntity.ok().body(result.getData());
    }
}
