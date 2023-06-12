package com.what2see.dto.tour;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourActionsResponseDTO {

    private @NotNull Boolean createReport;

    private @NotNull Boolean sendMessage;

    private @NotNull Boolean markAsCompleted;

    private @NotNull Boolean review;

    private @NotNull Boolean edit;

    private @NotNull Boolean delete;

    private @NotNull Boolean viewReports;

}
