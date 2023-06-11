package com.what2see.service.tour;

import com.what2see.exception.TourNotMarkedException;
import com.what2see.model.tour.Review;
import com.what2see.repository.tour.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review create(Review r) throws TourNotMarkedException {
        if(!r.getAuthor().getMarkedTours().contains(r.getTour()))
            throw new TourNotMarkedException(r.getTour(), r.getAuthor());
        return reviewRepository.save(r);
    }
}
