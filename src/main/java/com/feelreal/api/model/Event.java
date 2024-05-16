package com.feelreal.api.model;

import com.feelreal.api.model.enumeration.RepeatMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title")
    @NotNull
    @Length(min = 3, max = 64, message = "Title must be between 3 and 64 characters")
    private String title;

    @Column(name = "notes")
    @NotNull
    @Length(max = 500, message = "Notes must be less than or equal to 500 characters")
    private String notes;

    @Column(name = "date_time_start")
    @NotNull
    private LocalDateTime dateTimeStart;

    @Column(name = "date_time_end")
    @NotNull
    private LocalDateTime dateTimeEnd;

    @Column(name = "repeat_mode")
    @NotNull
    private RepeatMode repeatMode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public Event(
            String title,
            String notes,
            LocalDateTime dateTimeStart,
            LocalDateTime dateTimeEnd,
            RepeatMode repeatMode,
            User user
    ) {
        this.title = title;
        this.notes = notes;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.repeatMode = repeatMode;
        this.user = user;
    }

}
