package com.feelreal.api.dto.common;

import lombok.Getter;

@Getter
public enum ResultStatus {

    SUCCESS,

    DOES_NOT_EXIST,

    NO_PERMISSION,

    INVALID_INPUT,

    INTERNAL_ERROR

}
