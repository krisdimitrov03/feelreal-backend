package com.feelreal.api.repository;

import com.feelreal.api.model.Event;
import com.feelreal.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findByUser(User user);

}
