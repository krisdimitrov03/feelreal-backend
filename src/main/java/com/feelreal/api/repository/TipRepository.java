package com.feelreal.api.repository;

import com.feelreal.api.model.Tip;
import com.feelreal.api.model.enumeration.MoodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TipRepository extends JpaRepository<Tip, UUID> {

    List<Tip> findByType(MoodType type);

}
