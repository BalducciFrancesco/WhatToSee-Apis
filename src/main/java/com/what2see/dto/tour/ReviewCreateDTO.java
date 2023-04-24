package com.what2see.dto.tour;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@AllArgsConstructor
public class ReviewCreateDTO {

    // implicitly is current user in current timestamp in tour context

    private @NotNull @Range(min = 1, max = 5) Integer stars;

    private @NotBlank String description;

}
