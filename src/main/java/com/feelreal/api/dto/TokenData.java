package com.feelreal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenData {

    private String id;

    private String username;

    private String email;

    private String role;

}
