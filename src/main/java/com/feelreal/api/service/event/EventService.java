package com.feelreal.api.service.event;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.event.EventCreateRequest;
import com.feelreal.api.dto.event.EventEditRequest;
import com.feelreal.api.model.Event;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public interface EventService {

    OperationResult<UUID> create(EventCreateRequest data);

    OperationResult<Collection<Event>> getForUser(String token);

    OperationResult<Event> getById(UUID id, String token);

    OperationResult<UUID> update(EventEditRequest data);

    OperationResult<Object> delete(UUID id);

}
