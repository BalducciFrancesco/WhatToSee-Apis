package com.what2see.dto.tour;

import com.what2see.dto.user.GuideResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TourResponseDTO {

    private @NotNull Long id;

    private @NotNull GuideResponseDTO author;

    private @NotBlank String title;

    private @NotBlank String description;

    private @NotNull Boolean isPublic;

    private @NotNull CityResponseDTO city;

    private @NotNull List<TagResponseDTO> tags;     // can be empty

    private @NotNull ThemeResponseDTO theme;

    private @NotNull @PositiveOrZero Double approxCost;

    private @NotBlank String approxDuration;

    private @NotNull Date creationDate;

    private @NotEmpty List<TourStopResponseDTO> stops;

    private @NotNull List<ReviewResponseDTO> reviews;   // can be empty

}
