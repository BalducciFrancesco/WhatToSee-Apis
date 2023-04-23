package com.what2see.mapper.tour;

import com.what2see.dto.tour.StopCreateDTO;
import com.what2see.dto.tour.StopResponseDTO;
import com.what2see.model.tour.Stop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StopDTOMapper {

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
