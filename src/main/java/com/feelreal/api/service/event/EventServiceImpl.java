package com.feelreal.api.service.event;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.common.ResultStatus;
import com.feelreal.api.dto.event.EventCreateRequest;
import com.feelreal.api.dto.event.EventEditRequest;
import com.feelreal.api.model.Event;
import com.feelreal.api.model.User;
import com.feelreal.api.model.enumeration.RepeatMode;
import com.feelreal.api.repository.EventRepository;
import com.feelreal.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepo;

    private final UserRepository userRepo;

    @Autowired
    public EventServiceImpl(EventRepository repo, UserRepository userRepo) {
        this.eventRepo = repo;
        this.userRepo = userRepo;
    }

    @Override
    public OperationResult<UUID> create(EventCreateRequest data, UUID userId) {
        // TODO: Validate data

        try {
            Optional<User> user = userRepo.findById(userId);

            if (user.isEmpty()) {
                return new OperationResult<>(ResultStatus.INVALID_INPUT, null);
            }

            Event event = createEntity(data, user.get());
            eventRepo.saveAndFlush(event);
            return new OperationResult<>(ResultStatus.SUCCESS, event.getId());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new OperationResult<>(ResultStatus.INTERNAL_ERROR, null);
        }
    }

    @Override
    public OperationResult<Collection<Event>> getForUser(UUID userId) {
        List<Event> events = eventRepo.findByUserId(userId);

        return new OperationResult<>(ResultStatus.SUCCESS, events);
    }

    @Override
    public OperationResult<Event> getById(UUID id, UUID userId) {
        Optional<Event> eventOpt = eventRepo.findById(id);

        if (eventOpt.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        if (!eventOpt.get().getUser().getId().equals(userId)) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        return new OperationResult<>(ResultStatus.SUCCESS, eventOpt.get());
    }

    @Override
    public OperationResult<UUID> update(EventEditRequest data, UUID userId) {
        return new OperationResult<>(ResultStatus.SUCCESS, UUID.randomUUID());
    }

    @Override
    public OperationResult<Object> delete(UUID id, UUID userId) {
        return new OperationResult<>(ResultStatus.SUCCESS, null);
    }

    private Event createEntity(EventCreateRequest data, User user) {
        return new Event(
                data.getTitle(),
                data.getNotes(),
                LocalDateTime.parse(data.getDateTimeStart()),
                LocalDateTime.parse(data.getDateTimeEnd()),
                RepeatMode.fromInt(data.getRepeatMode()),
                user
        );
    }

}
