package com.feelreal.api.service.wellnessChecks;

import com.feelreal.api.model.WellnessCheck;
import java.util.List;
import java.util.UUID;

public interface WellnessCheckService {
    List<WellnessCheck> findAll();
    WellnessCheck findById(UUID id);
    WellnessCheck save(WellnessCheck wellnessCheck);
    WellnessCheck update(UUID id, WellnessCheck wellnessCheck);
    void delete(UUID id);
}
