package com.what2see.service.tour;

import com.what2see.EntityMock;
import com.what2see.exception.InteractionAlreadyPerformedException;
import com.what2see.exception.TourNotMarkedException;
import com.what2see.model.tour.Review;
import com.what2see.model.tour.Tour;
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
        Tour unreviewedTour;
        List<Tour> markedTours = mock.getTourist().getMarkedTours();
        List<Tour> reviewedTours = mock.getTourist().getReviewedTours().stream().map(Review::getTour).toList();
        unreviewedTour = markedTours.stream().filter(t -> !reviewedTours.contains(t)).findFirst().orElseThrow();
        Review expected = Review.builder()
                .author(mock.getTourist())
                .description("Review1")
                .tour(unreviewedTour)
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
        Tour unmarkedTour = mock.getAllTours().stream().filter(t -> !t.getMarkedTourists().contains(mock.getTourist())).findFirst().orElseThrow();
        Review expected = Review.builder()
                .author(mock.getTourist())
                .description("Review1")
                .tour(unmarkedTour)
                .stars(5)
                .build();
        // under test and assertion
        assertThrows(TourNotMarkedException.class, () -> reviewService.create(expected));
    }

    @Test
    void noInvalidRating() {
        // setup
        Tour unreviewedTour;
        List<Tour> markedTours = mock.getTourist().getMarkedTours();
        List<Tour> reviewedTours = mock.getTourist().getReviewedTours().stream().map(Review::getTour).toList();
        unreviewedTour = markedTours.stream().filter(t -> !reviewedTours.contains(t)).findFirst().orElseThrow();
        Review expected = Review.builder()
                .author(mock.getTourist())
                .description("Review1")
                .tour(unreviewedTour)
                .stars(6)
                .build();
        // under test and assertion
        assertThrows(ConstraintViolationException.class, () -> reviewService.create(expected));
    }

    @Test
    void noMultipleReviews() {
        // setup
        Review expected = Review.builder()
                .author(mock.getTourist())
                .description("Review1")
                .tour(mock.getTour())
                .stars(5)
                .build();
        // under test and assertion
        assertThrows(InteractionAlreadyPerformedException.class, () -> reviewService.create(expected));
    }

}