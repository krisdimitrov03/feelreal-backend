package com.feelreal.api.dto.authentication;

import lombok.Getter;

@Getter
public class UserUpdateRequest {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String jobId;

}
