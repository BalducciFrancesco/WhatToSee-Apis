package com.what2see.mapper.tour;

import com.what2see.dto.tour.TourStopCreateDTO;
import com.what2see.dto.tour.TourStopResponseDTO;
import com.what2see.model.tour.TourStop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TourStopDTOMapper {

    public TourStopResponseDTO convertResponse(TourStop stop) {
        return TourStopResponseDTO.builder()
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

    public TourStop convertCreate(TourStopCreateDTO t) {
        return TourStop.builder()
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
