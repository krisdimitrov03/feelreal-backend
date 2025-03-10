package com.feelreal.api.model;

import com.feelreal.api.model.enumeration.MoodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
@Data
@Table(name = "articles")
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "type")
    @NotNull
    private MoodType type;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "content")
    @Length(min = 1, max = 3000)
    @NotNull
    private String content;

    public Article(MoodType type, String title, String content) {
        this.type = type;
        this.title = title;
        this.content = content;
    }

}
