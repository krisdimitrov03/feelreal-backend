package com.feelreal.api.service.job;

import com.feelreal.api.model.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface JobService {

    Optional<Job> getById(UUID id);

    List<Job> getAll();

}
