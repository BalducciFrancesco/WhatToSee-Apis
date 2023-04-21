package com.what2see.controller.tour;


import com.what2see.dto.tour.TourCreateDTO;
import com.what2see.dto.tour.TourResponseDTO;
import com.what2see.dto.tour.TourSearchDTO;
import com.what2see.mapper.tour.TourDTOMapper;
import com.what2see.model.tour.Tour;
import com.what2see.service.tour.TagService;
import com.what2see.service.tour.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO how to know role?

@RequiredArgsConstructor
@RestController
@RequestMapping("/tour")
public class TourController {

    private final TourService tourService;

    private final TagService tagService;


    private final TourDTOMapper tourMapper;


    @GetMapping("/{tourId}")
    public ResponseEntity<TourResponseDTO> getById(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long guideId) {
        return ResponseEntity.ok(tourMapper.convertResponse(tourService.findById(tourId).orElseThrow()));
    }

    @PostMapping()
    public ResponseEntity<TourResponseDTO> create(@RequestBody @Valid TourCreateDTO t, @RequestHeader(value="Authentication") Long guideId) {
        tagService.create(t.getTagNames()); // create tags if not already exists
        Tour createdTour = tourService.create(tourMapper.convertCreate(t, guideId));
        return ResponseEntity.ok(tourMapper.convertResponse(createdTour));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourResponseDTO>> search(@Valid TourSearchDTO s, @RequestHeader(value="Authentication") Long guideId) {
        return ResponseEntity.ok(tourMapper.convertResponse(tourService.search(s)));
    }

}
