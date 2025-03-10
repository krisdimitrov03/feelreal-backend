package com.feelreal.api.contoller;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.event.EventCreateRequest;
import com.feelreal.api.config.jwt.UserPrincipal;
import com.feelreal.api.dto.event.EventDetailsResponse;
import com.feelreal.api.dto.event.EventEditRequest;
import com.feelreal.api.service.event.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailsResponse> getById(@PathVariable("id") UUID id, @AuthenticationPrincipal UserPrincipal user) {
        OperationResult<EventDetailsResponse> result = service.getById(id, user.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.OK).body(result.getData());
            case DOES_NOT_EXIST -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case INVALID_INPUT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case INTERNAL_ERROR -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @GetMapping
    public ResponseEntity<Collection<EventDetailsResponse>> getForUser(@AuthenticationPrincipal UserPrincipal user) {
        OperationResult<Collection<EventDetailsResponse>> result = service.getForUser(user.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.OK).body(result.getData());
            case INVALID_INPUT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody @Valid EventCreateRequest event, BindingResult bindingResult, @AuthenticationPrincipal UserPrincipal user) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        OperationResult<UUID> result = service.create(event, user.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case INVALID_INPUT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case DOES_NOT_EXIST -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            case INTERNAL_ERROR -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable("id") UUID id, @RequestBody @Valid EventEditRequest event, BindingResult bindingResult, @AuthenticationPrincipal UserPrincipal user) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        OperationResult<UUID> result = service.update(id, event, user.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.OK).body(result.getData());
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case INVALID_INPUT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case DOES_NOT_EXIST -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            case INTERNAL_ERROR -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID id, @AuthenticationPrincipal UserPrincipal user) {
        OperationResult<Object> result = service.delete(id, user.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case INVALID_INPUT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case DOES_NOT_EXIST -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            case INTERNAL_ERROR -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }
}
