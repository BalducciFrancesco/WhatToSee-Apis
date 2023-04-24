package com.what2see.controller.tour;


import com.what2see.dto.tour.*;
import com.what2see.mapper.tour.ReportDTOMapper;
import com.what2see.mapper.tour.ReviewDTOMapper;
import com.what2see.mapper.tour.TourDTOMapper;
import com.what2see.model.tour.Report;
import com.what2see.model.tour.Review;
import com.what2see.model.tour.Tour;
import com.what2see.service.tour.ReportService;
import com.what2see.service.tour.ReviewService;
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

    private final TourDTOMapper tourMapper;

    private final TagService tagService;

    private final ReviewService reviewService;

    private final ReviewDTOMapper reviewMapper;

    private final ReportService reportService;

    private final ReportDTOMapper reportMapper;


    @GetMapping("/{tourId}")
    public ResponseEntity<TourResponseDTO> getById(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long userId) {
        return ResponseEntity.ok(tourMapper.convertResponse(tourService.findById(tourId).orElseThrow()));
    }

    @PostMapping()
    public ResponseEntity<TourResponseDTO> create(@RequestBody @Valid TourCreateDTO t, @RequestHeader(value="Authentication") Long guideId) {
        tagService.create(t.getTagNames()); // create tags if not already exists
        Tour createdTour = tourService.create(tourMapper.convertCreate(t, guideId));
        return ResponseEntity.ok(tourMapper.convertResponse(createdTour));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourResponseDTO>> search(@Valid TourSearchDTO s, @RequestHeader(value="Authentication") Long userId) {
        return ResponseEntity.ok(tourMapper.convertResponse(tourService.search(s)));
    }

    @PostMapping("/{tourId}/review")
    public ResponseEntity<ReviewResponseDTO> createReview(@PathVariable Long tourId, @RequestBody @Valid ReviewCreateDTO r, @RequestHeader(value="Authentication") Long touristId) {
        Review createdReview = reviewService.create(reviewMapper.convertCreate(r, tourId, touristId));
        return ResponseEntity.ok(reviewMapper.convertResponse(createdReview));
    }

    @PostMapping("/{tourId}/report")
    public ResponseEntity<ReportResponseDTO> createReport(@PathVariable Long tourId, @RequestBody @Valid ReportCreateDTO r, @RequestHeader(value="Authentication") Long touristId) {
        Report createdReport = reportService.create(reportMapper.convertCreate(r, tourId, touristId));
        return ResponseEntity.ok(reportMapper.convertResponse(createdReport));
    }

    @PostMapping("/{tourId}/completed")
    public ResponseEntity<Void> markAsCompleted(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long touristId) {
        tourService.markAsCompleted(tourId, touristId);
        return ResponseEntity.ok().build();
    }

}
