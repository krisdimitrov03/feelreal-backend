package com.feelreal.api.contoller;

import com.feelreal.api.service.ai.AISuggestionService;
import com.feelreal.api.service.authentication.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final AISuggestionService service;

    @Autowired
    public AIController(AISuggestionService service, UserService userService) {
        this.service = service;
    }

    @GetMapping("/suggest")
    ResponseEntity<String> suggestActivity(@RequestParam String username) {
        String result = service.recommendPersonalizedActivity(username);

        return ResponseEntity.ok().body(result);
    }


}
