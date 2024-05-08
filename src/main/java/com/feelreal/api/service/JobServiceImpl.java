package com.feelreal.api.service;

import com.feelreal.api.model.Job;
import com.feelreal.api.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository repo;

    @Autowired
    public JobServiceImpl(JobRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Job> getById(UUID id) {
        return repo.findById(id);
    }
}
