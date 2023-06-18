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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for tour endpoints
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/tour")
public class TourController {

    // dependencies autowired by spring boot

    private final TourService tourService;

    private final TourDTOMapper tourMapper;

    private final TagService tagService;

    private final ReviewService reviewService;

    private final ReviewDTOMapper reviewMapper;

    private final ReportService reportService;

    private final ReportDTOMapper reportMapper;

    private final UserService<User> userService;

    private final UserService<Tourist> touristService;

    private final UserService<Guide> guideService;

    private final UserService<Administrator> administratorService;

    private final UserDTOMapper userMapper;


    /*
     * Some validations are not explicitly performed with try/catch's since RuntimeExceptions are expected to
     * be called and managed from the Spring Boot container in case of failed validation or user not found.
     */

    // -------
    // GET
    // -------

    /**
     * Get a specific tour by its ID
     * @param tourId wanted tour
     * @param userId requesting user
     * @return wanted tour DTO
     * @exception ResponseStatusException {@link HttpStatus#UNAUTHORIZED} if user is not authorized to see the wanted tour
     */
    @GetMapping("/{tourId}")
    public ResponseEntity<TourResponseDTO> getById(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long userId) {
        Tour t = tourService.findById(tourId);
        userService.findById(userId);
        if(!tourService.isVisible(t, userId)) { // ensure user (tourist, guide or administrator) can actually see the wanted tour
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a visualizzare questo tour");
        }
        return ResponseEntity.ok(tourMapper.convertResponse(t));
    }

    /**
     * Get all tours that match <b>all</b> specified criteria.<br>
     * For tags, it is sufficient that at least one is present.<br>
     * For maximum duration, it is sufficient that it is lower or equal.
     * @param s DTO representing criteria (city and/or tags and/or theme and/or maximum duration) to search with
     * @param userId requesting user
     * @return list of tours that match all specified criteria
     */
    @GetMapping("/search")
    public ResponseEntity<List<TourResponseDTO>> search(@Valid TourSearchDTO s, @RequestHeader(value="Authentication") Long userId) {
        User u = userService.findById(userId);
        return ResponseEntity.ok(tourMapper.convertResponse(tourService.search(u, s)));
    }

    /**
     * Get the interactions that the requesting user is able to perform on the specified tour.<br>
     * @param tourId tour to check
     * @param userId requesting user
     * @return list of available actions as key (actionName) / value (boolean) DTO
     * @see TourActionsResponseDTO
     */
    @GetMapping("/{tourId}/availableActions")
    public ResponseEntity<TourActionsResponseDTO> getAvailableActions(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long userId) {
        Tour t = tourService.findById(tourId);
        userService.findById(userId);
        return ResponseEntity.ok(this.tourService.getAvailableActions(t, userId));
    }

    /**
     * Get the list of <i>users</i> that have been shared the specified tour with and therefore are able to see it even if private.<br>
     * It is intended to only be used by the tour author guide.
     * @param tourId tour to check
     * @param guideId requesting guide (tour's author)
     * @return list of users that have been shared the specified tour with
     * @exception ResponseStatusException {@link HttpStatus#UNAUTHORIZED} if user is not tour's author
     */
    @GetMapping("/{tourId}/shared")
    public ResponseEntity<List<UserResponseDTO>> getSharedTourists(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long guideId) {
        Tour t = tourService.findById(tourId);
        Guide g = guideService.findById(guideId);
        if(!t.getAuthor().equals(g)) {  // ensure guide is actually the tour's author
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a visualizzare le condivisioni di questo tour");
        }
        return ResponseEntity.ok(t.getSharedTourists().stream().map(userMapper::convertResponse).collect(Collectors.toList()));
    }

    /**
     * Get the list of tours that have been shared with the requesting user and therefore is able to see even if private.<br>
     * @param touristId requesting tourist
     * @return list of tours that have been shared with the requesting user
     */
    @GetMapping("/shared")
    public ResponseEntity<List<TourResponseDTO>> getSharedWithMe(@RequestHeader(value="Authentication") Long touristId) {
        Tourist t = touristService.findById(touristId);
        return ResponseEntity.ok(tourMapper.convertResponse(t.getSharedTours()));
    }

    /**
     * Get the list of tours that have at have been reported at least once
     * @param administratorId requesting administrator
     * @return list of tours that have at have been reported at least once
     */
    @GetMapping("/reported")
    public ResponseEntity<List<TourResponseDTO>> getReported(@RequestHeader(value="Authentication") Long administratorId) {
        administratorService.findById(administratorId);
        return ResponseEntity.ok(tourMapper.convertResponse(tourService.findAllByReported(true)));
    }

    /**
     * Get the list of tours that have been created by the requesting guide
     * @param guideId requesting guide
     * @return list of tours that have been created by the requesting guide
     */
    @GetMapping("/created")
    public ResponseEntity<List<TourResponseDTO>> getCreatedByMe(@RequestHeader(value="Authentication") Long guideId) {
        Guide g = guideService.findById(guideId);
        return ResponseEntity.ok(tourMapper.convertResponse(g.getCreatedTours()));
    }

    /**
     * Get the list of <i>reports</i> created by tourists onto the specified tour
     * @param tourId tour to check
     * @param administratorId requesting administrator
     * @return list of reports created by tourists onto the specified tour
     */
    @GetMapping("/{tourId}/report")
    public ResponseEntity<List<ReportResponseDTO>> getReportsById(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long administratorId) {
        administratorService.findById(administratorId);
        List<Report> reports = tourService.findById(tourId).getReports();
        return ResponseEntity.ok(reports.stream().map(reportMapper::convertResponse).collect(Collectors.toList()));
    }

    /**
     * Get the list of tours that have been completed by the requesting tourist
     * @param touristId requesting tourist
     * @return list of tours that have been completed by the requesting tourist
     */
    @GetMapping("/completed")
    public ResponseEntity<List<TourResponseDTO>> getCompletedByMe(@RequestHeader(value="Authentication") Long touristId) {
        Tourist t = touristService.findById(touristId);
        return ResponseEntity.ok(tourMapper.convertResponse(tourService.getCompletedTours(t)));
    }

    // -------
    // PATCH
    // -------

    /**
     * Edit the specified tour with the provided data.<br>
     * It is intended to only be used by the tour author guide.
     * @param editedTour DTO representing the data to update the tour with.
     *                   It is essentially the same object that is returned by {@link TourController#create} method, 
     *                   with modifications to the fields that are expected to be changed.
     * @param tourId tour to edit
     * @param guideId requesting guide (must be the tour's author)
     * @return the updated tour
     * @exception ResponseStatusException {@link HttpStatus#UNAUTHORIZED} if user is not tour's author
     */
    @PatchMapping("/{tourId}")
    public ResponseEntity<TourResponseDTO> editById(@RequestBody @Valid TourCreateDTO editedTour, @PathVariable Long tourId, @RequestHeader(value="Authentication") Long guideId) {
        Tour oldTour = tourService.findById(tourId);
        Guide g = guideService.findById(guideId);
        if(!oldTour.getAuthor().equals(g)) {  // ensure guide is actually the tour's author
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a modificre questo tour");
        }
        tourService.update(oldTour, tourMapper.convertCreate(editedTour, guideId));
        return ResponseEntity.ok(tourMapper.convertResponse(oldTour));
    }

    // -------
    // DELETE
    // -------

    /**
     * Delete the specified tour.<br>
     * It is intended to only be used by the tour author guide or by an administrator.
     * @param tourId tour to delete
     * @param userId requesting guide (must be the tour's author or an administrator)
     * @return {@link HttpStatus#OK} (empty response body) if the tour has been deleted successfully
     * @exception ResponseStatusException {@link HttpStatus#UNAUTHORIZED} if user is not tour's author or an administrator
     */
    @DeleteMapping("/{tourId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long tourId, @RequestHeader(value="Authentication") Long userId) {
        Tour t = tourService.findById(tourId);
        userService.findById(userId);
        if(!tourService.isDeletable(t, userId)) {   // ensure user is actually the tour's author or an administrator
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non sei autorizzato a eliminare questo tour");
        }
        tourService.delete(t);
        return ResponseEntity.ok().build();
    }

    // -------
    // POST
    // -------

    /**
     * Create a new tour with the provided data.<br>
     * The title must be unique and tags will be created on the fly if not already existing.
     * @param t DTO representing the data to create the tour with
     * @param guideId requesting guide
     * @return the created tour
     * @exception ResponseStatusException {@link HttpStatus#BAD_REQUEST} if the tour name is already taken
     */
    @PostMapping()
    public ResponseEntity<TourResponseDTO> create(@RequestBody @Valid TourCreateDTO t, @RequestHeader(value="Authentication") Long guideId) {
        guideService.findById(guideId);
        tagService.createByNames(t.getTagNames()); // create tags if not already existing
        Tour created;
        try {
            created = tourService.create(tourMapper.convertCreate(t, guideId));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome tour già esistente");
        }
        return ResponseEntity.ok(tourMapper.convertResponse(created));
    }

    /**
     * Leave a new review on the specified tour.<br>
     * It is intended to only be used by a tourist that has actually marked the tour as {@link TourController#markAsCompletedByMe completed} and only once per tour.
     * @param tourId tour to review
     * @param r DTO representing the data to create the review with
     * @param touristId requesting tourist
     * @return the created review
     * @exception ResponseStatusException {@link HttpStatus#BAD_REQUEST} if the tour was not marked as completed or if was already reviewed by the requesting tourist
     */
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

    /**
     * Leave a new report on the specified tour.<br>
     * It is intended to only be used by a tourist once per tour.
     * @param tourId tour to report
     * @param r DTO representing the data to create the report with
     * @param touristId requesting tourist
     * @return the created report
     * @exception ResponseStatusException {@link HttpStatus#BAD_REQUEST} if the tour was already reported by the requesting tourist
     */
    @PostMapping("/{tourId}/report")
    public ResponseEntity<ReportResponseDTO> createReport(@PathVariable Long tourId, @RequestBody @Valid ReportCreateDTO r, @RequestHeader(value="Authentication") Long touristId) {
        touristService.findById(touristId);
        Report createdReport;
        try {
            createdReport = reportService.create(reportMapper.convertCreate(r, tourId, touristId));
        } catch (InteractionAlreadyPerformedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hai già segnalato questo tour");
        }
        return ResponseEntity.ok(reportMapper.convertResponse(createdReport));
    }

    /**
     * Mark the specified tour as completed by the requesting tourist.<br>
     * It is intended to only be used by a tourist once per tour.
     * @param tourId tour to mark as completed
     * @param touristId requesting tourist
     * @return {@link HttpStatus#OK} (empty response body) if the tour has been marked as completed successfully
     * @exception ResponseStatusException {@link HttpStatus#BAD_REQUEST} if the tour was already marked as completed by the requesting tourist
     */
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
