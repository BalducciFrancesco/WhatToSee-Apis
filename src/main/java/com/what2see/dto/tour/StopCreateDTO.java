package com.what2see.dto.tour;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementation of <b>DTO</b> pattern for <i>creating</i> stops in the <b>incoming</b> body.<br>
 * Usually is used as a part of a tour creation request.
 * @see com.what2see.model.tour.Stop
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StopCreateDTO {

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
