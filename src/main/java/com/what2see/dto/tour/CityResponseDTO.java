package com.what2see.dto.tour;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CityResponseDTO {

    private @NotBlank Long id;

    private @NotBlank String name;

}
