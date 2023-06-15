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

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review create(Review r) throws TourNotMarkedException, InteractionAlreadyPerformedException {
        Tour creatingTour = r.getTour();
        Tourist creatingTourist = r.getAuthor();

        if(!creatingTourist.getMarkedTours().contains(creatingTour))
            throw new TourNotMarkedException(creatingTour, creatingTourist);
        if(creatingTourist.getReviewedTours().stream().anyMatch(rr -> rr.getTour().equals(creatingTour)))
            throw new InteractionAlreadyPerformedException(creatingTour, creatingTourist);

        return reviewRepository.save(r);
    }
}
