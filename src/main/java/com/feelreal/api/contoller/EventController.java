package com.feelreal.api.contoller;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.event.EventCreateRequest;
import com.feelreal.api.config.jwt.UserPrincipal;
import com.feelreal.api.model.Event;
import com.feelreal.api.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("/{id}")

    public ResponseEntity<Event> getById(@PathVariable("id") UUID id, @AuthenticationPrincipal UserPrincipal user) {
        OperationResult<Event> result = service.getById(id, user.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.OK).body(result.getData());
            case DOES_NOT_EXIST -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case INVALID_INPUT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case INTERNAL_ERROR -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @GetMapping("/")
    public ResponseEntity<Collection<Event>> getForUser(@AuthenticationPrincipal UserPrincipal user) {
        OperationResult<Collection<Event>> result = service.getForUser(user.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.OK).body(result.getData());
            case INVALID_INPUT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @PostMapping("/")
    public ResponseEntity<UUID> create(@RequestBody EventCreateRequest event, @AuthenticationPrincipal UserPrincipal user) {
        OperationResult<UUID> result = service.create(event, user.getId());

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
            case NO_PERMISSION -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case INVALID_INPUT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            case DOES_NOT_EXIST -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            case INTERNAL_ERROR -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }
}
