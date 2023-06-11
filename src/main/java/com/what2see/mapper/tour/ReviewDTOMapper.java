package com.what2see.mapper.tour;

import com.what2see.dto.tour.ReviewCreateDTO;
import com.what2see.dto.tour.ReviewResponseDTO;
import com.what2see.mapper.user.UserDTOMapper;
import com.what2see.model.tour.Review;
import com.what2see.model.user.Tourist;
import com.what2see.service.tour.TourService;
import com.what2see.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class ReviewDTOMapper {

    private final UserDTOMapper userMapper;

    private final UserService<Tourist> touristService;

    private final TourService tourService;

    public ReviewResponseDTO convertResponse(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .author(userMapper.convertResponse(review.getAuthor()))
                .timestamp(review.getTimestamp())
                .stars(review.getStars())
                .description(review.getDescription())
                .build();
    }

    public Review convertCreate(ReviewCreateDTO r, Long tourId, Long touristAuthorId) {
        return Review.builder()
                .timestamp(new Date())
                .stars(r.getStars())
                .description(r.getDescription())
                .tour(tourService.findById(tourId))
                .author(touristService.findById(touristAuthorId))
                .build();
    }
}
