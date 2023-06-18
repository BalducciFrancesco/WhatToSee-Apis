package com.what2see.dto.tour;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementation of <b>DTO</b> pattern for <i>creating</i> reports in the <b>incoming</b> body.<br>
 * Information about the author is implicitly obtained from other request parameters
 * @see com.what2see.model.tour.Report
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportCreateDTO {

    private @NotBlank String description;

}
