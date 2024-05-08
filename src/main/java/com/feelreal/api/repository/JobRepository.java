package com.feelreal.api.repository;

import com.feelreal.api.model.Job;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    @NotNull
    public Optional<Job> findById(UUID id);

}
