package com.what2see.dto.tour;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementation of <b>DTO</b> pattern for <i>returning</i> tour available interactions in the <b>outgoing</b> body.<br>
 * Its purpose is to provide the client a way to dynamically show or hide UI elements based on actually performable actions.<br>
 * Each property name is an action that could be performed on a tour. Its value is whether the action is available or not.
 */
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
