package com.feelreal.api.contoller;

import com.feelreal.api.dto.RegisterDto;
import com.feelreal.api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService service){
        userService = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody RegisterDto data) {
        boolean status = userService.register(data);

        return ResponseEntity.ok().body(status);
    }
}
