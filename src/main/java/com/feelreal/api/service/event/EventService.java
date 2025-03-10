package com.feelreal.api.service.event;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.event.EventCreateRequest;
import com.feelreal.api.dto.event.EventDetailsResponse;
import com.feelreal.api.dto.event.EventEditRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public interface EventService {

    OperationResult<UUID> create(EventCreateRequest data, UUID userId);

    OperationResult<Collection<EventDetailsResponse>> getForUser(UUID userId);

    OperationResult<EventDetailsResponse> getById(UUID id, UUID userId);

    OperationResult<UUID> update(UUID eventId, EventEditRequest data, UUID userId);

    OperationResult<Object> delete(UUID id, UUID userId);

}
