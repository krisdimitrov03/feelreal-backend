package com.feelreal.api.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
public class EventCreateRequest {

    @NotNull
    @Length(min = 3, max = 64)
    private String title;

    @NotNull
    @Length(max = 500)
    private String notes;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dateTimeStart;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dateTimeEnd;

    @NotNull
    @Range(min = 0, max = 3)
    private int repeatMode;

}
