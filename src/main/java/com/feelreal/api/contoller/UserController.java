package com.feelreal.api.contoller;

import com.feelreal.api.config.jwt.UserPrincipal;
import com.feelreal.api.dto.authentication.*;
import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.service.authentication.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/users")
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
            @RequestBody @Valid RegisterRequest data,
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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest data) {

        return userService
                .login(data).map(s -> {
                            logger.atInfo().log("User logged in: {}", data.getUsername());
                            return ResponseEntity.ok().body(new LoginResponse(true, s));
                        }
                ).orElseGet(() -> ResponseEntity.ok().body(new LoginResponse(false, null)));

    }

    @GetMapping("/authenticate")
    public ResponseEntity<Boolean> authenticate(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> viewProfile(@PathVariable("id") UUID id, @AuthenticationPrincipal UserPrincipal principal) {
        OperationResult<UserProfile> result = userService.getProfile(id, principal.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.ok().body(result.getData());
            case DOES_NOT_EXIST -> ResponseEntity.notFound().build();
            default -> ResponseEntity.badRequest().build();
        };
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateProfileResult> updateProfile(@PathVariable("id") UUID id, @RequestBody UserUpdateRequest data, @AuthenticationPrincipal UserPrincipal principal) {
        OperationResult<UpdateProfileResult> result = userService.updateProfile(id, data, principal.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.ok().body(result.getData());
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case DOES_NOT_EXIST -> ResponseEntity.notFound().build();
            case INVALID_INPUT -> ResponseEntity.badRequest().build();
            case INTERNAL_ERROR -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> deleteProfile(@PathVariable("id") UUID id, @AuthenticationPrincipal UserPrincipal principal) {
        OperationResult<UUID> result = userService.deleteProfile(id, principal.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.noContent().build();
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case DOES_NOT_EXIST -> ResponseEntity.notFound().build();
            case INTERNAL_ERROR -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            default -> ResponseEntity.badRequest().build();
        };
    }

}
