package com.feelreal.api.contoller;

import com.feelreal.api.config.jwt.UserPrincipal;
import com.feelreal.api.model.Article;
import com.feelreal.api.service.ai.AISuggestionService;
import com.feelreal.api.service.authentication.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final AISuggestionService aiSuggestionService;

    @Autowired
    public AIController(AISuggestionService aiSuggestionService) {
        this.aiSuggestionService = aiSuggestionService;
    }

    @GetMapping("/suggest")
    public ResponseEntity<Article> suggestArticle(@AuthenticationPrincipal UserPrincipal user) {
        Article result = aiSuggestionService.recommendPersonalizedActivity(user.getId());

        return ResponseEntity.ok().body(result);
    }
}
