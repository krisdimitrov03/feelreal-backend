package com.feelreal.api.model.enumeration;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    private Role(String value) {
        this.value = value;
    }

}
