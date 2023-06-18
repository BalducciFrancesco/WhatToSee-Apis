package com.what2see.mapper.tour;

import com.what2see.dto.tour.TourCreateDTO;
import com.what2see.dto.tour.TourResponseDTO;
import com.what2see.mapper.user.UserDTOMapper;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.service.tour.CityService;
import com.what2see.service.tour.TagService;
import com.what2see.service.tour.ThemeService;
import com.what2see.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that converts {@link Tour} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class TourDTOMapper {

    // dependencies autowired by spring boot

    private final UserService<Tourist> touristService;

    private final UserService<Guide> guideService;

    private final UserDTOMapper userMapper;

    private final CityService cityService;

    private final CityDTOMapper cityMapper;

    private final TagService tagService;

    private final TagDTOMapper tagMapper;

    private final ThemeDTOMapper themeMapper;

    private final ThemeService themeService;

    private final StopDTOMapper stopMapper;

    private final ReviewDTOMapper reviewMapper;

    /**
     * Converts a {@link Tour} entity to a {@link TourResponseDTO DTO} that can be sent to client
     * @param tour entity to be converted
     * @return DTO that can be sent to client
     */
    public TourResponseDTO convertResponse(Tour tour) {
        return TourResponseDTO.builder()
                .id(tour.getId())
                .author(userMapper.convertResponse(tour.getAuthor()))
                .title(tour.getTitle())
                .description(tour.getDescription())
                .isPublic(tour.isPublic())
                .city(cityMapper.convertResponse(tour.getCity()))
                .tags(tour.getTags().stream().map(tagMapper::convertResponse).collect(Collectors.toList()))
                .theme(themeMapper.convertResponse(tour.getTheme()))
                .approxCost(tour.getApproxCost())
                .approxDuration(tour.getApproxDuration())
                .creationDate(tour.getCreationDate())
                .stops(tour.getStops().stream().map(stopMapper::convertResponse).collect(Collectors.toList()))
                .reviews(tour.getReviews().stream().map(reviewMapper::convertResponse).collect(Collectors.toList()))
                .markedAsCompletedCount((long) tour.getMarkedTourists().size())
                .build();
    }

    /**
     * Converts a list of {@link Tour} entities to a list of {@link TourResponseDTO DTOs} that can be sent to client
     * @param tour list of entities to be converted
     * @return list of DTOs that can be sent to client
     */
    public List<TourResponseDTO> convertResponse(List<Tour> tour) {
        return tour.stream().map(this::convertResponse).collect(Collectors.toList());
    }

    /**
     * Converts a {@link TourCreateDTO DTO} to a {@link Tour entity} that can be persisted
     * @param t DTO to be converted
     * @param guideAuthorId id of the guide that created the tour
     * @return entity that can be persisted
     */
    public Tour convertCreate(TourCreateDTO t, Long guideAuthorId) {
        Tour tour = Tour.builder()
                .title(t.getTitle())
                .description(t.getDescription())
                .approxCost(t.getApproxCost())
                .approxDuration(t.getApproxDuration())
                .creationDate(new Date())
                .isPublic(t.getIsPublic())
                .author(guideService.findById(guideAuthorId))
                .stops(t.getStops().stream().map(stopMapper::convertCreate).collect(Collectors.toList()))
                .reports(new ArrayList<>())
                .reviews(new ArrayList<>())
                .city(cityService.findById(t.getCityId()))
                .theme(themeService.findById(t.getThemeId()))
                .tags(t.getTagNames() != null ? tagService.findAllByNames(t.getTagNames()) : new ArrayList<>()) // ignoring ids as some tags may not exist yet
                .sharedTourists(!t.getIsPublic() && t.getSharedTouristIds() != null ? t.getSharedTouristIds().stream().map(touristService::findById).collect(Collectors.toList()) : new ArrayList<>())  // if tour is private, consider shared tourists
                .markedTourists(new ArrayList<>())
                .build();
        tour.getStops().forEach(s -> s.setTour(tour));  // important because of single-side relation ownership
        return tour;
    }

}
