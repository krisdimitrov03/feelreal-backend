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
import com.feelreal.api.service.authentication.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepo;

    private final UserService userService;

    @Autowired
    public EventServiceImpl(EventRepository repo, UserService userService) {
        this.eventRepo = repo;
        this.userService = userService;
    }

    @Override
    public OperationResult<UUID> create(EventCreateRequest data, UUID userId) {
        Optional<User> user = userService.getById(userId);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        try {

            Event event = createEntity(data, user.get());
            eventRepo.saveAndFlush(event);
            return new OperationResult<>(ResultStatus.SUCCESS, event.getId());

        } catch (Exception e) {
            return new OperationResult<>(ResultStatus.INTERNAL_ERROR, null);
        }
    }

    @Override
    public OperationResult<Collection<Event>> getForUser(UUID userId) {
        Optional<User> user = userService.getById(userId);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        List<Event> events = eventRepo.findByUser(user.get());

        return new OperationResult<>(ResultStatus.SUCCESS, events);
    }

    @Override
    public OperationResult<Event> getById(UUID id, UUID userId) {
        Optional<Event> event = eventRepo.findById(id);

        if (event.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        if (!event.get().getUser().getId().equals(userId)) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        return new OperationResult<>(ResultStatus.SUCCESS, event.get());
    }

    @Override
    public OperationResult<UUID> update(UUID eventId, EventEditRequest data, UUID userId) {
        Optional<Event> event = eventRepo.findById(eventId);

        if (event.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        if (!event.get().getUser().getId().equals(userId)) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        try {
            Event updated = updateEntity(event.get(), data);
            eventRepo.saveAndFlush(updated);
            return new OperationResult<>(ResultStatus.SUCCESS, updated.getId());
        } catch (Exception e) {
            return new OperationResult<>(ResultStatus.INTERNAL_ERROR, null);
        }
    }

    @Override
    public OperationResult<Object> delete(UUID id, UUID userId) {
        Optional<Event> event = eventRepo.findById(id);

        if (event.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        if (!event.get().getUser().getId().equals(userId)) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        try {
            eventRepo.delete(event.get());
            eventRepo.flush();
        } catch (Exception e) {
            return new OperationResult<>(ResultStatus.INTERNAL_ERROR, null);
        }

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

    private Event updateEntity(Event event, EventEditRequest data) {
        if (data.getTitle() != null) {
            event.setTitle(data.getTitle());
        }

        if (data.getNotes() != null) {
            event.setNotes(data.getNotes());
        }

        if (data.getDateTimeStart() != null) {
            event.setDateTimeStart(LocalDateTime.parse(data.getDateTimeStart()));
        }

        if (data.getDateTimeEnd() != null) {
            event.setDateTimeEnd(LocalDateTime.parse(data.getDateTimeEnd()));
        }

        if (data.getRepeatMode() != -1) {
            event.setRepeatMode(RepeatMode.fromInt(data.getRepeatMode()));
        }

        return event;
    }

}
