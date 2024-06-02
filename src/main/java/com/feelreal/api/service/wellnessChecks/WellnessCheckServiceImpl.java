package com.feelreal.api.service.wellnessChecks;

import com.feelreal.api.model.WellnessCheck;
import com.feelreal.api.repository.WellnessCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WellnessCheckServiceImpl implements WellnessCheckService {

    @Autowired
    private WellnessCheckRepository wellnessCheckRepository;

    @Override
    public List<WellnessCheck> findAll() {
        return wellnessCheckRepository.findAll();
    }

    @Override
    public WellnessCheck findById(UUID id) {
        return wellnessCheckRepository.findById(id).orElse(null);
    }

    @Override
    public WellnessCheck save(WellnessCheck wellnessCheck) {
        return wellnessCheckRepository.save(wellnessCheck);
    }

    @Override
    public WellnessCheck update(UUID id, WellnessCheck wellnessCheck) {
        if (!wellnessCheckRepository.existsById(id)) {
            return null;
        }
        wellnessCheck.setId(id);
        return wellnessCheckRepository.save(wellnessCheck);
    }

    @Override
    public void delete(UUID id) {
        wellnessCheckRepository.deleteById(id);
    }
}

