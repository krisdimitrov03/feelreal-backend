package com.feelreal.api.repository;

import com.feelreal.api.model.WellnessCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WellnessCheckRepository extends JpaRepository<WellnessCheck, UUID> {

    List<WellnessCheck> findAllByUserId(UUID userId);

    void deleteByUserId(UUID userId);

}
