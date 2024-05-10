package com.feelreal.api.model;

import com.feelreal.api.model.enumeration.Intensity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
@Data
@Table(name = "jobs")
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", unique = true)
    @NotNull
    @Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Column(name = "intensity")
    @NotNull
    private Intensity intensity;

    public Job(String name, Intensity intensity) {
        this.name = name;
        this.intensity = intensity;
    }

}


