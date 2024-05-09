package com.feelreal.api.contoller;

import com.feelreal.api.dto.LoginDto;
import com.feelreal.api.dto.LoginResultDto;
import com.feelreal.api.dto.RegisterDto;
import com.feelreal.api.dto.RegisterResponse;
import com.feelreal.api.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final Logger logger;

    @Autowired
    public UserController(UserService service) {
        userService = service;
        logger = LoggerFactory.getLogger(UserController.class);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody @Valid RegisterDto data,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(new RegisterResponse(false, errors));
        }

        RegisterResponse response = userService.register(data);

        if (response.isSuccessful()) {
            logger.atInfo().log("User registered: {}", data.getUsername());

            return ResponseEntity.ok().body(new RegisterResponse(true, Collections.emptyList()));
        }

        return ResponseEntity.ok().body(new RegisterResponse(false, response.getErrors()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResultDto> login(@RequestBody LoginDto data) {

        return userService
                .login(data).map(s -> {
                            logger.atInfo().log("User logged in: {}", data.getUsername());
                            return ResponseEntity.ok().body(new LoginResultDto(true, s));
                        }
                ).orElseGet(() -> ResponseEntity.ok().body(new LoginResultDto(false, null)));

    }
}
