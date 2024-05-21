package com.feelreal.api.dto.event;

import com.feelreal.api.model.enumeration.RepeatMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class EventDetailsResponse {

    private UUID id;

    private String title;

    private String notes;

    private String dateTimeStart;

    private String dateTimeEnd;

    private RepeatMode repeatMode;

}
