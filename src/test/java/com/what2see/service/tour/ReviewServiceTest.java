package com.what2see.service.tour;

import com.what2see.EntityMock;
import com.what2see.exception.InteractionAlreadyPerformedException;
import com.what2see.exception.TourNotMarkedException;
import com.what2see.model.tour.Review;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Tourist;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ReviewService}.
 */
@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ReviewServiceTest {

    // dependencies autowired by spring boot

    private final EntityMock mock;

    private final ReviewService reviewService;

    /**
     * Tests {@link ReviewService#create(Review)} in the successful case.<br>
     * Ensures that a new review (with a new id and the same description) is created.
     */
    @Test
    void create() {
        // setup
        Tourist subject = mock.getTourist();
        // find a tour that HAS NOT been reviewed but HAS been marked by the subject (otherwise the test would fail)
        List<Long> subjectReviewedToursIds = subject.getReviewedTours().stream().map(Review::getTour).map(Tour::getId).toList();
        List<Long> subjectMarkedToursIds = subject.getMarkedTours().stream().map(Tour::getId).toList();
        Tour unreviewedMarkedTour = mock.getTourist().getMarkedTours().stream().filter(t -> !subjectReviewedToursIds.contains(t.getId()) && subjectMarkedToursIds.contains(t.getId())).findAny().orElseThrow();
        String expectedDescription = "Review1";
        Review expected = Review.builder()
                .author(mock.getTourist())
                .description(expectedDescription)
                .tour(unreviewedMarkedTour)
                .stars(5)
                .build();
        // under test
        Review underTest = reviewService.create(expected);
        // assertion
        assertNotNull(underTest.getId());
        assertEquals(expected.getDescription(), underTest.getDescription());
    }

    /**
     * Tests {@link ReviewService#create(Review)} in the unsuccessful case because the review's author has not marked the tour as completed.<br>
     * Ensures that a {@link TourNotMarkedException} is thrown.
     */
    @Test
    void noUnmarkedTourReview() {
        // setup
        Tourist subject = mock.getTourist();
        // find a tour that HAS NOT been reviewed and HAS NOT been marked by the subject (otherwise the test would fail)
        List<Long> subjectReviewedToursIds = subject.getReviewedTours().stream().map(Review::getTour).map(Tour::getId).toList();
        List<Long> subjectMarkedToursIds = subject.getMarkedTours().stream().map(Tour::getId).toList();
        Tour unreviewedUnmarkedTour = mock.getAllTours().stream().filter(t -> !subjectReviewedToursIds.contains(t.getId()) && !subjectMarkedToursIds.contains(t.getId())).findAny().orElseThrow();
        String expectedDescription = "Review2";
        Review expected = Review.builder()
                .author(subject)
                .description(expectedDescription)
                .tour(unreviewedUnmarkedTour)
                .stars(5)
                .build();
        // under test and assertion
        assertThrows(TourNotMarkedException.class, () -> reviewService.create(expected));
    }

    /**
     * Tests {@link ReviewService#create(Review)} in the unsuccessful case because the review's author has put invalid rating (less than 1 or more than 5 stars).<br>
     * Ensures that a {@link ConstraintViolationException} is thrown.
     */
    @Test
    void noInvalidRating() {
        // setup
        Tourist subject = mock.getTourist();
        // find a tour that HAS NOT been reviewed but HAS been marked by the subject (otherwise the test would fail)
        List<Long> subjectReviewedToursIds = subject.getReviewedTours().stream().map(Review::getTour).map(Tour::getId).toList();
        List<Long> subjectMarkedToursIds = subject.getMarkedTours().stream().map(Tour::getId).toList();
        Tour unreviewedMarkedTour = mock.getAllTours().stream().filter(t -> !subjectReviewedToursIds.contains(t.getId()) && subjectMarkedToursIds.contains(t.getId())).findAny().orElseThrow();
        String expectedDescription = "Review3";
        Review expected = Review.builder()
                .author(mock.getTourist())
                .description(expectedDescription)
                .tour(unreviewedMarkedTour)
                .stars(6)   // invalid rating
                .build();
        // under test and assertion
        assertThrows(ConstraintViolationException.class, () -> reviewService.create(expected));
    }

    /**
     * Tests {@link ReviewService#create(Review)} in the unsuccessful case because the review's author has already reviewed the tour.<br>
     * Ensures that a {@link InteractionAlreadyPerformedException} is thrown.
     */
    @Test
    void noMultipleReviews() {
        // setup
        Tourist subject = mock.getTourist();
        // find a tour that HAS been reviewed and HAS been marked by the subject (otherwise the test would fail)
        List<Long> subjectReviewedToursIds = subject.getReviewedTours().stream().map(Review::getTour).map(Tour::getId).toList();
        List<Long> subjectMarkedToursIds = subject.getMarkedTours().stream().map(Tour::getId).toList();
        Tour reviewedMarkedTour = mock.getAllTours().stream().filter(t -> subjectReviewedToursIds.contains(t.getId()) && subjectMarkedToursIds.contains(t.getId())).findAny().orElseThrow();
        String expectedDescription = "Review4";
        Review expected = Review.builder()
                .author(subject)
                .description(expectedDescription)
                .tour(reviewedMarkedTour)
                .stars(5)
                .build();
        // under test and assertion
        assertThrows(InteractionAlreadyPerformedException.class, () -> reviewService.create(expected));
    }

}