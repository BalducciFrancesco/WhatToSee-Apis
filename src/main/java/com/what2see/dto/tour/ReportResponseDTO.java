package com.what2see.dto.tour;

import com.what2see.dto.user.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReportResponseDTO {

    private @NotNull Long id;

    private @NotNull UserResponseDTO author;    // tourist

    private @NotBlank String description;

}
