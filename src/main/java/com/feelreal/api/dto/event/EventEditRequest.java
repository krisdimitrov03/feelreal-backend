package com.feelreal.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class EventEditRequest {

    private UUID id;

    private String title;

    private String notes;

    private String dateTimeStart;

    private String dateTimeEnd;

    private int repeatMode;

}
