package com.what2see.dto.tour;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReportCreateDTO {

    // implicitly is current user in current timestamp

    private @NotNull Long tourId;

    private @NotBlank String description;

}
