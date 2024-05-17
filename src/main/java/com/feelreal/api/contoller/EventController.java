package com.feelreal.api.contoller;

import com.feelreal.api.config.jwt.UserPrincipal;
import com.feelreal.api.dto.common.OperationResult;
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
    public ResponseEntity<Collection<Event>> getForUser(@RequestHeader("Authorization") String authorization) {
        OperationResult<Collection<Event>> result = service.getForUser(authorization.replace("Bearer ", ""));

        return switch (result.getStatus()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.OK).body(result.getData());
            case INVALID_INPUT -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

}
