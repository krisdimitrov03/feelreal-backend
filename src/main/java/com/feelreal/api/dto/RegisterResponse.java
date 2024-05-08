package com.feelreal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RegisterResponse {

    private boolean successful;

    private List<String> errors;

}

