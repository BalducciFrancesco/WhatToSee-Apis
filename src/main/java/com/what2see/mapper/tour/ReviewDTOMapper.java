package com.what2see.mapper.tour;

import com.what2see.dto.tour.ReviewCreateDTO;
import com.what2see.dto.tour.ReviewResponseDTO;
import com.what2see.mapper.user.TouristDTOMapper;
import com.what2see.model.tour.Review;
import com.what2see.repository.tour.TourRepository;
import com.what2see.repository.user.TouristRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ReviewDTOMapper {

    private final TouristDTOMapper touristMapper;

    private final TouristRepository touristRepository;

    private final TourRepository tourRepository;

    public ReviewResponseDTO convertResponse(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .author(touristMapper.convertResponse(review.getAuthor()))
                .timestamp(review.getTimestamp())
                .stars(review.getStars())
                .description(review.getDescription())
                .build();
    }

    public Review convertCreate(ReviewCreateDTO r, Long touristAuthorId) throws NoSuchElementException {
        return Review.builder()
                .timestamp(new Date())
                .stars(r.getStars())
                .description(r.getDescription())
                .tour(tourRepository.findById(r.getTourId()).orElseThrow())
                .author(touristRepository.findById(touristAuthorId).orElseThrow())
                .build();
    }
}
