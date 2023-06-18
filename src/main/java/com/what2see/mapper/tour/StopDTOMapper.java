package com.what2see.mapper.tour;

import com.what2see.dto.tour.StopCreateDTO;
import com.what2see.dto.tour.StopResponseDTO;
import com.what2see.model.tour.Stop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service that converts {@link Stop} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class StopDTOMapper {

    /**
     * Converts a {@link Stop} entity to a {@link StopResponseDTO DTO} that can be sent to client
     * @param stop entity to be converted
     * @return DTO that can be sent to client
     */
    public StopResponseDTO convertResponse(Stop stop) {
        return StopResponseDTO.builder()
                .id(stop.getId())
                .title(stop.getTitle())
                .description(stop.getDescription())
                .cost(stop.getCost())
                .duration(stop.getDuration())
                .transferCost(stop.getTransferCost())
                .transferDuration(stop.getTransferDuration())
                .transferType(stop.getTransferType())
                .transferDetails(stop.getTransferDetails())
                .transferOtherOptions(stop.getTransferOtherOptions())
                .build();
    }

    /**
     * Converts a client-sent {@link StopCreateDTO DTO} to a {@link Stop} entity that can be persisted
     * @param t DTO to be converted
     * @return entity that can be persisted
     */
    public Stop convertCreate(StopCreateDTO t) {
        return Stop.builder()
                .title(t.getTitle())
                .description(t.getDescription())
                .cost(t.getCost())
                .duration(t.getDuration())
                .transferCost(t.getTransferCost())
                .transferDuration(t.getTransferDuration())
                .transferType(t.getTransferType())
                .transferDetails(t.getTransferDetails())
                .transferOtherOptions(t.getTransferOtherOptions())
                .build();
    }
}
