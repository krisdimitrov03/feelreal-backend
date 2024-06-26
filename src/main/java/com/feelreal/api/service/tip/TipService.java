package com.feelreal.api.service.tip;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.model.Tip;
import com.feelreal.api.model.enumeration.MoodType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface TipService {

    OperationResult<Tip> getById(UUID id);

    OperationResult<List<Tip>> getAll();

    OperationResult<List<Tip>> getByType(MoodType type);

    OperationResult<Tip> getRandomByType(MoodType type);

}
