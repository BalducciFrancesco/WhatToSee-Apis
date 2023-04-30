package com.what2see.controller.tour;


import com.what2see.dto.tour.*;
import com.what2see.dto.user.TouristResponseDTO;
import com.what2see.exception.TourNotMarkedException;
import com.what2see.mapper.tour.ReportDTOMapper;
import com.what2see.mapper.tour.ReviewDTOMapper;
import com.what2see.mapper.tour.TourDTOMapper;
import com.what2see.mapper.user.TouristDTOMapper;
import com.what2see.model.tour.Report;
import com.what2see.model.tour.Review;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.service.tour.ReportService;
import com.what2see.service.tour.ReviewService;
import com.what2see.service.tour.TagService;
import com.what2see.service.tour.TourService;
import com.what2see.service.user.GuideService;
import com.what2see.service.user.TouristService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    private final GuideService guideService;

    private final TouristService touristService;

    private final TouristDTOMapper touristMapper;



    @GetMapping("/{tourId}")
    public ResponseEntity<TourResponseDTO> getById(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long userId) {
        Tour t = tourService.findById(tourId);
        if(!tourService.checkVisibility(t, userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a visualizzare questo tour");
        }
        return ResponseEntity.ok(tourMapper.convertResponse(t));
    }

    @PatchMapping("/{tourId}")
    public ResponseEntity<TourResponseDTO> editById(@RequestBody @Valid TourCreateDTO t, @PathVariable Long tourId, @RequestHeader(value="Authentication") Long guideId) {
        Tour existingTour = tourService.findById(tourId);
        Guide g = guideService.findById(guideId);
        if(!existingTour.getAuthor().equals(g)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a modificre questo tour");
        }
        tourService.update(existingTour, tourMapper.convertCreate(t, guideId));
        return ResponseEntity.ok(tourMapper.convertResponse(existingTour));
    }

    @DeleteMapping("/{tourId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long guideId) {
        Tour t = tourService.findById(tourId);
        Guide g = guideService.findById(guideId);
        if(!t.getAuthor().equals(g)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a eliminare questo tour");
        }
        tourService.delete(t);
        return ResponseEntity.ok().build();
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

    @GetMapping("/{tourId}/shared")
    public ResponseEntity<List<TouristResponseDTO>> getSharedTourists(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long guideId) {
        Tour t = tourService.findById(tourId);
        Guide g = guideService.findById(guideId);
        if(!t.getAuthor().equals(g)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a visualizzare le condivisioni di questo tour");
        }
        return ResponseEntity.ok(touristMapper.convertResponse(t.getSharedTourists()));
    }

    @GetMapping("/shared")
    public ResponseEntity<List<TourResponseDTO>> getSharedWithMe(@RequestHeader(value="Authentication") Long touristId) {
        Tourist t = touristService.findById(touristId);
        return ResponseEntity.ok(tourMapper.convertResponse(t.getSharedTours()));
    }

    @GetMapping("/created")
    public ResponseEntity<List<TourResponseDTO>> getCreated(@RequestHeader(value="Authentication") Long guideId) {
        Guide g = guideService.findById(guideId);
        return ResponseEntity.ok(tourMapper.convertResponse(g.getCreatedTours()));
    }

    @PostMapping("/{tourId}/review")
    public ResponseEntity<ReviewResponseDTO> createReview(@PathVariable Long tourId, @RequestBody @Valid ReviewCreateDTO r, @RequestHeader(value="Authentication") Long touristId) {
        try {
            Review createdReview = reviewService.create(reviewMapper.convertCreate(r, tourId, touristId));
            return ResponseEntity.ok(reviewMapper.convertResponse(createdReview));
        } catch (TourNotMarkedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non è possibile recensire tour che non sono stati segnati come percorsi");
        }
    }

    @PostMapping("/{tourId}/report")
    public ResponseEntity<ReportResponseDTO> createReport(@PathVariable Long tourId, @RequestBody @Valid ReportCreateDTO r, @RequestHeader(value="Authentication") Long touristId) {
        Report createdReport = reportService.create(reportMapper.convertCreate(r, tourId, touristId));
        return ResponseEntity.ok(reportMapper.convertResponse(createdReport));
    }

    @GetMapping("/completed")
    public ResponseEntity<List<TourResponseDTO>> getCompleted(@RequestHeader(value="Authentication") Long touristId) {
        Tourist t = touristService.findById(touristId);
        return ResponseEntity.ok(tourMapper.convertResponse(t.getMarkedTours()));
    }

    @PostMapping("/{tourId}/completed")
    public ResponseEntity<Void> markAsCompleted(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long touristId) {
        Tour t = tourService.findById(tourId);
        Tourist tt = touristService.findById(touristId);
        tourService.markAsCompleted(t, tt);
        return ResponseEntity.ok().build();
    }

}
