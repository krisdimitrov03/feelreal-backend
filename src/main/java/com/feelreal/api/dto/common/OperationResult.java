package com.feelreal.api.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationResult<T> {

    private ResultStatus status;

    private T data;

}
