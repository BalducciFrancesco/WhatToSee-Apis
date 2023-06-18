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

/**
 * Service that converts {@link Review} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class ReviewDTOMapper {

    // dependencies autowired by spring boot

    private final UserDTOMapper userMapper;

    private final UserService<Tourist> touristService;

    private final TourService tourService;

    /**
     * Converts a {@link Review} entity to a {@link ReviewResponseDTO DTO} that can be sent to client
     * @param review entity to be converted
     * @return DTO that can be sent to client
     */
    public ReviewResponseDTO convertResponse(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .author(userMapper.convertResponse(review.getAuthor()))
                .timestamp(review.getTimestamp())
                .stars(review.getStars())
                .description(review.getDescription())
                .build();
    }

    /**
     * Converts a client-sent {@link ReviewCreateDTO DTO} to a {@link Review} entity that can be persisted
     * @param r DTO to be converted
     * @param tourId id of the tour to be reviewed
     * @param touristAuthorId id of the tourist that is reviewing
     * @return entity that can be persisted
     */
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
