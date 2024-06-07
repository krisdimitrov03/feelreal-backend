package com.feelreal.api.service.wellnessChecks;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.wellnesschecks.WellnessCheckRequest;
import com.feelreal.api.dto.wellnesschecks.WellnessCheckResponse;

import java.util.List;
import java.util.UUID;

public interface WellnessCheckService {
    OperationResult<List<WellnessCheckResponse>> getForUser(UUID userId);

    OperationResult<WellnessCheckResponse> getById(UUID id, UUID userId);

    OperationResult<UUID> create(WellnessCheckRequest data, UUID userId);
    OperationResult<UUID> update(UUID id, WellnessCheckRequest data, UUID userId);
    OperationResult<UUID> delete(UUID id, UUID userId);
}
