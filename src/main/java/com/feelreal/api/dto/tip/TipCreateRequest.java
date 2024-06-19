package com.feelreal.api.dto.tip;

import lombok.Data;

@Data
public class TipCreateRequest {

    private int type;

    private String content;

}
