package com.what2see.dto.tour;

import com.what2see.dto.user.TouristResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class ReviewResponseDTO {

    private @NotNull Long id;

    private @NotNull TouristResponseDTO author;

    private @NotNull Date timestamp;

    private @NotNull @Range(min = 0, max = 5) Integer stars;

    private @NotBlank String description;

}
