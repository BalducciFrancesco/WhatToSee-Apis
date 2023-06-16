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

@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ReviewServiceTest {

    private final EntityMock mock;

    private final ReviewService reviewService;


    @Test
    void create() {
        // setup
        Tourist subject = mock.getTourist();
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

    @Test
    void noUnmarkedTourReview() {
        // setup
        Tourist subject = mock.getTourist();
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

    @Test
    void noInvalidRating() {
        // setup
        Tourist subject = mock.getTourist();
        List<Long> subjectReviewedToursIds = subject.getReviewedTours().stream().map(Review::getTour).map(Tour::getId).toList();
        List<Long> subjectMarkedToursIds = subject.getMarkedTours().stream().map(Tour::getId).toList();
        Tour unreviewedMarkedTour = mock.getAllTours().stream().filter(t -> !subjectReviewedToursIds.contains(t.getId()) && subjectMarkedToursIds.contains(t.getId())).findAny().orElseThrow();
        String expectedDescription = "Review3";
        Review expected = Review.builder()
                .author(mock.getTourist())
                .description(expectedDescription)
                .tour(unreviewedMarkedTour)
                .stars(6)
                .build();
        // under test and assertion
        assertThrows(ConstraintViolationException.class, () -> reviewService.create(expected));
    }

    @Test
    void noMultipleReviews() {
        // setup
        Tourist subject = mock.getTourist();
        List<Long> subjectReviewedToursIds = subject.getReviewedTours().stream().map(Review::getTour).map(Tour::getId).toList();
        List<Long> subjectMarkedToursIds = subject.getMarkedTours().stream().map(Tour::getId).toList();
        Tour reviewedMarkedTour = mock.getAllTours().stream().filter(t -> subjectReviewedToursIds.contains(t.getId()) && subjectMarkedToursIds.contains(t.getId())).findAny().orElseThrow();
        String expectedDescription = "Review4";
        Review expected = Review.builder()
                .author(mock.getTourist())
                .description(expectedDescription)
                .tour(mock.getTour())
                .stars(5)
                .build();
        // under test and assertion
        assertThrows(InteractionAlreadyPerformedException.class, () -> reviewService.create(expected));
    }

}