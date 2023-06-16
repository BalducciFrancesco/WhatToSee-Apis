package com.what2see.controller.tour;


import com.what2see.dto.tour.*;
import com.what2see.dto.user.UserResponseDTO;
import com.what2see.exception.InteractionAlreadyPerformedException;
import com.what2see.exception.TourNotMarkedException;
import com.what2see.mapper.tour.ReportDTOMapper;
import com.what2see.mapper.tour.ReviewDTOMapper;
import com.what2see.mapper.tour.TourDTOMapper;
import com.what2see.mapper.user.UserDTOMapper;
import com.what2see.model.tour.Report;
import com.what2see.model.tour.Review;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Administrator;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.model.user.User;
import com.what2see.service.tour.ReportService;
import com.what2see.service.tour.ReviewService;
import com.what2see.service.tour.TagService;
import com.what2see.service.tour.TourService;
import com.what2see.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


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

    private final UserDTOMapper userMapper;
    private final UserService<User> userService;
    private final UserService<Tourist> touristService;
    private final UserService<Guide> guideService;
    private final UserService<Administrator> administratorService;


    // -------
    // GET
    // -------

    @GetMapping("/{tourId}")
    public ResponseEntity<TourResponseDTO> getById(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long userId) {
        Tour t = tourService.findById(tourId);
        if(!tourService.isVisible(t, userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a visualizzare questo tour");
        }
        return ResponseEntity.ok(tourMapper.convertResponse(t));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourResponseDTO>> search(@Valid TourSearchDTO s, @RequestHeader(value="Authentication") Long userId) {
        User u = userService.findById(userId);
        return ResponseEntity.ok(tourMapper.convertResponse(tourService.search(u, s)));
    }

    @GetMapping("/{tourId}/availableActions")
    public ResponseEntity<TourActionsResponseDTO> getAvailableActions(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long userId) {
        Tour t = tourService.findById(tourId);
        return ResponseEntity.ok(this.tourService.getAvailableActions(t, userId));
    }

    @GetMapping("/{tourId}/shared")
    public ResponseEntity<List<UserResponseDTO>> getSharedTourists(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long guideId) {
        Tour t = tourService.findById(tourId);
        Guide g = guideService.findById(guideId);
        if(!t.getAuthor().equals(g)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a visualizzare le condivisioni di questo tour");
        }
        return ResponseEntity.ok(t.getSharedTourists().stream().map(userMapper::convertResponse).collect(Collectors.toList()));
    }

    @GetMapping("/shared")
    public ResponseEntity<List<TourResponseDTO>> getSharedWithMe(@RequestHeader(value="Authentication") Long touristId) {
        Tourist t = touristService.findById(touristId);
        return ResponseEntity.ok(tourMapper.convertResponse(t.getSharedTours()));
    }

    @GetMapping("/reported")
    public ResponseEntity<List<TourResponseDTO>> getReported(@RequestHeader(value="Authentication") Long administratorId) {
        administratorService.findById(administratorId);
        return ResponseEntity.ok(tourMapper.convertResponse(tourService.findAllByReported(true)));
    }

    @GetMapping("/created")
    public ResponseEntity<List<TourResponseDTO>> getCreatedByMe(@RequestHeader(value="Authentication") Long guideId) {
        Guide g = guideService.findById(guideId);
        return ResponseEntity.ok(tourMapper.convertResponse(g.getCreatedTours()));
    }

    @GetMapping("/{tourId}/report")
    public ResponseEntity<List<ReportResponseDTO>> getReportsById(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long administratorId) {
        administratorService.findById(administratorId);
        List<Report> reports = tourService.findById(tourId).getReports();
        return ResponseEntity.ok(reports.stream().map(reportMapper::convertResponse).collect(Collectors.toList()));
    }

    @GetMapping("/completed")
    public ResponseEntity<List<TourResponseDTO>> getCompletedByMe(@RequestHeader(value="Authentication") Long touristId) {
        Tourist t = touristService.findById(touristId);
        return ResponseEntity.ok(tourMapper.convertResponse(t.getMarkedTours()));
    }

    // -------
    // PATCH
    // -------

    @PatchMapping("/{tourId}")
    public ResponseEntity<TourResponseDTO> editById(@RequestBody @Valid TourCreateDTO editedTour, @PathVariable Long tourId, @RequestHeader(value="Authentication") Long guideId) {
        Tour oldTour = tourService.findById(tourId);
        if(!tourService.isEditable(oldTour, guideId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a modificre questo tour");
        }
        tourService.update(oldTour, tourMapper.convertCreate(editedTour, guideId));
        return ResponseEntity.ok(tourMapper.convertResponse(oldTour));
    }

    // -------
    // DELETE
    // -------

    @DeleteMapping("/{tourId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long userId) {
        Tour t = tourService.findById(tourId);
        if(!tourService.isDeletable(t, userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a eliminare questo tour");
        }
        tourService.delete(t);
        return ResponseEntity.ok().build();
    }

    // -------
    // POST
    // -------

    @PostMapping()
    public ResponseEntity<TourResponseDTO> create(@RequestBody @Valid TourCreateDTO t, @RequestHeader(value="Authentication") Long guideId) {
        guideService.findById(guideId);
        tagService.createByNames(t.getTagNames()); // create tags if not already existing
        Tour createdTour = tourService.create(tourMapper.convertCreate(t, guideId));
        return ResponseEntity.ok(tourMapper.convertResponse(createdTour));
    }

    @PostMapping("/{tourId}/review")
    public ResponseEntity<ReviewResponseDTO> createReview(@PathVariable Long tourId, @RequestBody @Valid ReviewCreateDTO r, @RequestHeader(value="Authentication") Long touristId) {
        touristService.findById(touristId);
        Review createdReview;
        try {
            createdReview = reviewService.create(reviewMapper.convertCreate(r, tourId, touristId));
        } catch (TourNotMarkedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non è possibile recensire tour che non sono stati segnati come percorsi");
        } catch (InteractionAlreadyPerformedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hai già recensito questo tour");
        }
        return ResponseEntity.ok(reviewMapper.convertResponse(createdReview));
    }


    @PostMapping("/{tourId}/report")
    public ResponseEntity<ReportResponseDTO> createReport(@PathVariable Long tourId, @RequestBody @Valid ReportCreateDTO r, @RequestHeader(value="Authentication") Long touristId) {
        touristService.findById(touristId);
        Report createdReport = reportService.create(reportMapper.convertCreate(r, tourId, touristId));
        return ResponseEntity.ok(reportMapper.convertResponse(createdReport));
    }

    @PostMapping("/{tourId}/completed")
    public ResponseEntity<Void> markAsCompletedByMe(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long touristId) {
        Tour t = tourService.findById(tourId);
        Tourist tt = touristService.findById(touristId);
        try {
            tourService.markAsCompleted(t, tt);
        } catch (InteractionAlreadyPerformedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hai già segnato come percorso questo tour");
        }
        return ResponseEntity.ok().build();
    }

}
