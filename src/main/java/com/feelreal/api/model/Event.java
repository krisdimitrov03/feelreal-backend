package com.feelreal.api.model;

import com.feelreal.api.model.enumeration.RepeatMode;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "notes", nullable = false)
    private String notes;

    @Column(name = "date_time_start", nullable = false)
    private LocalDateTime dateTimeStart;

    @Column(name = "date_time_end", nullable = false)
    private LocalDateTime dateTimeEnd;

    @Column(name = "repeat_mode", nullable = false)
    private RepeatMode repeatMode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
}
