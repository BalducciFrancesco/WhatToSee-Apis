package com.what2see.dto.tour;

import com.what2see.dto.user.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementation of <b>DTO</b> pattern for <i>returning</i> reports in the <b>outgoing</b> body
 * @see com.what2see.model.tour.Report
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDTO {

    private @NotNull Long id;

    private @NotNull UserResponseDTO author;    // tourist

    private @NotBlank String description;

}
