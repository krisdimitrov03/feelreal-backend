package com.feelreal.api.service.wellnessChecks;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.wellnesschecks.WellnessCheckDetailsResponse;

import java.util.List;
import java.util.UUID;

public interface WellnessCheckService {
    OperationResult<List<WellnessCheckDetailsResponse>> getForUser(UUID userId);

    OperationResult<WellnessCheckDetailsResponse> getById(UUID id, UUID userId);

    OperationResult<UUID> create(WellnessCheckDetailsResponse data, UUID userId);
    OperationResult<UUID> update(UUID id, WellnessCheckDetailsResponse data, UUID userId);
    OperationResult<UUID> delete(UUID id, UUID userId);
}
