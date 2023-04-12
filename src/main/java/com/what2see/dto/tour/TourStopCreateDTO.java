package com.what2see.dto.tour;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TourStopCreateDTO {

    private @NotBlank String title;

    private @NotBlank String description;

    private @NotNull Double cost;

    private @NotBlank String duration;

    private @NotNull Double transferCost;

    private @NotBlank String transferDuration;

    private @NotBlank String transferType;

    private @NotBlank String transferDetails;

    private String transferOtherOptions;

}
