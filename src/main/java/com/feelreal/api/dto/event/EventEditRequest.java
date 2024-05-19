package com.feelreal.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventEditRequest {

    private String title;

    private String notes;

    private String dateTimeStart;

    private String dateTimeEnd;

    private int repeatMode = -1;

}
