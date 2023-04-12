package com.what2see.controller.tour;


import com.what2see.dto.tour.TourCreateDTO;
import com.what2see.dto.tour.TourResponseDTO;
import com.what2see.mapper.tour.TourDTOMapper;
import com.what2see.model.tour.Tour;
import com.what2see.service.tour.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tour")
public class TourController {

    private final TourService tourService;

    private final TourDTOMapper tourMapper;

    // TODO check if is guide
    @PostMapping()
    public ResponseEntity<TourResponseDTO> create(@RequestBody @Valid TourCreateDTO t, @RequestHeader(value="Authentication") Long guideId) {
        Tour createdTour = tourService.create(t, guideId);
        return ResponseEntity.ok(this.tourMapper.convertResponse(createdTour));
    }

}
