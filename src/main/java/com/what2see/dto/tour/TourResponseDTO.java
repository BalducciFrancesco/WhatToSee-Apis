package com.what2see.dto.tour;

import com.what2see.dto.user.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Implementation of <b>DTO</b> pattern for <i>returning</i> tours in the <b>outgoing</b> body
 * @see com.what2see.model.tour.Tour
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourResponseDTO {

    private @NotNull Long id;

    private @NotNull UserResponseDTO author;    // guide

    private @NotBlank String title;

    private @NotBlank String description;

    private @NotNull Boolean isPublic;

    private @NotNull CityResponseDTO city;

    private @NotNull List<TagResponseDTO> tags;

    private @NotNull ThemeResponseDTO theme;

    private @NotNull @PositiveOrZero Double approxCost;

    private @NotBlank String approxDuration;

    private @NotNull Date creationDate;

    private @NotEmpty List<StopResponseDTO> stops;

    private @NotNull List<ReviewResponseDTO> reviews;

    private @NotNull Long markedAsCompletedCount;   // how many tourists tagged as completed

}
