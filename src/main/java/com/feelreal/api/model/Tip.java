package com.feelreal.api.model;

import com.feelreal.api.model.enumeration.MoodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "tips")
@NoArgsConstructor
public class Tip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "type")
    @NotNull
    private MoodType type;

    @Column(name = "content")
    @NotNull
    private String content;

    public Tip(MoodType type, String content) {
        this.type = type;
        this.content = content;
    }

}
