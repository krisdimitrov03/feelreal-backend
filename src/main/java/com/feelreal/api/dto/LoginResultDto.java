package com.feelreal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResultDto {

    private boolean successful;

    private String token;

}
