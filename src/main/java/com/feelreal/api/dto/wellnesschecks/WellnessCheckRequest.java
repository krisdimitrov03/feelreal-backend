package com.feelreal.api.dto.wellnesschecks;

import com.feelreal.api.model.enumeration.WellnessCheckType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WellnessCheckRequest {

    @NotNull
    private WellnessCheckType type;

    @NotNull
    @Range(min = 1, max = 12)
    private short value;

    @NotNull
    @DateTimeFormat
    private String date;

    @NotNull
    private UUID userId;

}