package com.feelreal.api.service.tip;

import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.common.ResultStatus;
import com.feelreal.api.model.Tip;
import com.feelreal.api.model.enumeration.MoodType;
import com.feelreal.api.repository.TipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TipServiceImpl implements TipService {

    private final TipRepository tipRepository;

    @Autowired
    public TipServiceImpl(TipRepository tipRepository) {
        this.tipRepository = tipRepository;
    }

    @Override
    public OperationResult<Tip> getById(UUID id) {
        return tipRepository.findById(id)
                .map(value -> new OperationResult<>(ResultStatus.SUCCESS, value))
                .orElseGet(() -> new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null));

    }

    @Override
    public OperationResult<List<Tip>> getAll() {
        return new OperationResult<>(ResultStatus.SUCCESS, tipRepository.findAll());
    }

    @Override
    public OperationResult<List<Tip>> getByType(MoodType type) {
        return new OperationResult<>(ResultStatus.SUCCESS, tipRepository.findByType(type));
    }

    @Override
    public OperationResult<Tip> getRandomByType(MoodType type) {
        var tips = getByType(type).getData();

        int index = (int) (Math.random() * tips.size());

        return new OperationResult<>(ResultStatus.SUCCESS, tips.get(index));
    }
}
