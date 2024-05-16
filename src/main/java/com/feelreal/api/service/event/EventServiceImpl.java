package com.feelreal.api.service.event;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.common.ResultStatus;
import com.feelreal.api.dto.event.EventCreateRequest;
import com.feelreal.api.dto.event.EventEditRequest;
import com.feelreal.api.model.Event;
import com.feelreal.api.repository.EventRepository;
import com.feelreal.api.service.authentication.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repo;

    @Autowired
    public EventServiceImpl(EventRepository repo, JwtService jwtService) {
        this.repo = repo;
    }

    @Override
    public OperationResult<UUID> create(EventCreateRequest data) {
        return new OperationResult<>(ResultStatus.SUCCESS, UUID.randomUUID());
    }

    @Override
    public OperationResult<Collection<Event>> getForUser(String username) {

        return null;


//        List<Event> events = repo.findByUserId(userId);
//
//        return new OperationResult<>(ResultStatus.SUCCESS, events);
    }

    @Override
    public OperationResult<Event> getById(UUID id, UUID userId) {
        Optional<Event> eventOpt = repo.findById(id);

        if (eventOpt.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        if (!eventOpt.get().getUser().getId().equals(userId)) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        return new OperationResult<>(ResultStatus.SUCCESS, eventOpt.get());
    }

    @Override
    public OperationResult<UUID> update(EventEditRequest data) {
        return new OperationResult<>(ResultStatus.SUCCESS, UUID.randomUUID());
    }

    @Override
    public OperationResult<Object> delete(UUID id) {
        return new OperationResult<>(ResultStatus.SUCCESS, null);
    }
}
