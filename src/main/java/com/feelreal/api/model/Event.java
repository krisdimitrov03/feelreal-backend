package com.feelreal.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
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

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time_start", nullable = false)
    private Time timeStart;

    @Column(name = "time_end", nullable = false)
    private Time timeEnd;

    @ManyToOne
    @JoinColumn(name = "repeat_mode_id")
    private RepeatMode repeatMode;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
}
