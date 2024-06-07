package com.feelreal.api.contoller;

import com.feelreal.api.config.jwt.UserPrincipal;
import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.wellnesschecks.WellnessCheckRequest;
import com.feelreal.api.dto.wellnesschecks.WellnessCheckResponse;
import com.feelreal.api.model.WellnessCheck;
import com.feelreal.api.service.wellnessChecks.WellnessCheckService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wellness-checks")
public class WellnessCheckController {

    private final WellnessCheckService wellnessCheckService;

    @Autowired
    public WellnessCheckController(WellnessCheckService wellnessCheckService) {
        this.wellnessCheckService = wellnessCheckService;
    }

    @GetMapping
    public ResponseEntity<List<WellnessCheckResponse>> getForUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        OperationResult<List<WellnessCheckResponse>> result = wellnessCheckService
                .getForUser(userPrincipal.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.ok(result.getData());
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            default -> ResponseEntity.badRequest().build();
        };
    }

    @GetMapping("/{id}")
    public ResponseEntity<WellnessCheckResponse> getById(@PathVariable UUID id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        OperationResult<WellnessCheckResponse> result = wellnessCheckService.getById(id, userPrincipal.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.ok(result.getData());
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case DOES_NOT_EXIST -> ResponseEntity.notFound().build();
            default -> ResponseEntity.badRequest().build();
        };
    }

    @PostMapping
    public ResponseEntity<UUID> create(
            @RequestBody @Valid WellnessCheckRequest wellnessCheck,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        OperationResult<UUID> result = wellnessCheckService.create(wellnessCheck, userPrincipal.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.ok(result.getData());
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            default -> ResponseEntity.badRequest().build();
        };

    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(
            @PathVariable UUID id,
            @RequestBody @Valid WellnessCheckRequest wellnessCheck,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        OperationResult<UUID> result = wellnessCheckService.update(id, wellnessCheck, userPrincipal.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.ok(result.getData());
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case DOES_NOT_EXIST -> ResponseEntity.notFound().build();
            default -> ResponseEntity.badRequest().build();
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> delete(@PathVariable UUID id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        OperationResult<UUID> result = wellnessCheckService.delete(id, userPrincipal.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.noContent().build();
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case DOES_NOT_EXIST -> ResponseEntity.notFound().build();
            default -> ResponseEntity.badRequest().build();
        };
    }
}
