package com.what2see.service.tour;

import com.what2see.exception.InteractionAlreadyPerformedException;
import com.what2see.exception.TourNotMarkedException;
import com.what2see.model.tour.Review;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Tourist;
import com.what2see.repository.tour.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class that handles the business logic for {@link Review} entities.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    // dependencies autowired by spring boot

    private final ReviewRepository reviewRepository;

    /**
     * Creates a new {@link Review} entity.
     * @param r review to be created (without id)
     * @return created review (with id)
     * @throws TourNotMarkedException if the author (tourist) of the review has not previously marked the tour
     * @throws InteractionAlreadyPerformedException if the author (tourist) of the review has already reviewed the tour
     */
    public Review create(Review r) throws TourNotMarkedException, InteractionAlreadyPerformedException {
        Tour creatingTour = r.getTour();
        Tourist creatingTourist = r.getAuthor();

        // check if the tourist has not previously marked the tour
        if(!creatingTourist.getMarkedTours().contains(creatingTour))
            throw new TourNotMarkedException(creatingTour, creatingTourist);

        // check if the tourist has already reviewed the tour
        if(creatingTourist.getReviewedTours().stream().anyMatch(rr -> rr.getTour().equals(creatingTour)))
            throw new InteractionAlreadyPerformedException(creatingTour, creatingTourist);

        return reviewRepository.save(r);
    }
}
