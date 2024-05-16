package com.feelreal.api.config.jwt;

import com.feelreal.api.model.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserPrincipal {

    private UUID id;

    private String username;

    private Role role;

}
