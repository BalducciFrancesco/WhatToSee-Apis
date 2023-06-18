package com.what2see.dto.tour;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementation of <b>DTO</b> pattern for <i>returning</i> themes in the <b>outgoing</b> body
 * @see com.what2see.model.tour.Theme
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThemeResponseDTO {

    private @NotBlank Long id;

    private @NotBlank String name;

}
