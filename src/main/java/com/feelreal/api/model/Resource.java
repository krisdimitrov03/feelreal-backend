package com.feelreal.api.model;

import com.feelreal.api.model.enumeration.ResourceType;
import jakarta.persistence.*;
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
    private ResourceType type;

    @Column(name = "content")
    private byte[] content;

}
