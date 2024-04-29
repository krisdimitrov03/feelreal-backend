package com.feelreal.api.model;

import com.feelreal.api.model.enumeration.ResourceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "type")
    @NotNull
    private ResourceType type;

    @Column(name = "content")
    @NotNull
    private byte[] content;

}
