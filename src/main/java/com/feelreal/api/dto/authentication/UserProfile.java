package com.feelreal.api.dto.authentication;

import com.feelreal.api.model.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    private UUID id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private Job job;

}
