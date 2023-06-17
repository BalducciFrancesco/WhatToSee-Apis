package com.what2see.dto.tour;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourCreateDTO {

    private @NotBlank String title;

    private @NotBlank String description;

    private @NotNull Long cityId;

    private @NotNull List<String> tagNames;    // can be empty and created if not existing

    private @NotNull Long themeId;

    private @NotNull @PositiveOrZero Double approxCost;

    private @NotBlank String approxDuration;

    private @NotNull Boolean isPublic;

    private List<Long> sharedTouristIds;    // can be null only if public (ignored anyway)

    private @NotEmpty List<StopCreateDTO> stops;

}
