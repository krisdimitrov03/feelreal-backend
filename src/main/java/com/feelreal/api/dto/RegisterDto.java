package com.feelreal.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.UUID;

@Data
public class RegisterDto {

    @NotNull
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String password;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String dateOfBirth;

    @NotNull
    private UUID jobId;

}
