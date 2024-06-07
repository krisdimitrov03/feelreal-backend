package com.feelreal.api.dto.wellnesschecks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WellnessCheckResponse {

    private UUID id;

    private int type;

    private int value;

    private String date;

    private UUID userId;

}
