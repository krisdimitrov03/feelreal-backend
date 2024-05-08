package com.feelreal.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

}
