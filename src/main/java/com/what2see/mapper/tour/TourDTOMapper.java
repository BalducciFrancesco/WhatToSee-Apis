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

@RequiredArgsConstructor
@Service
public class TourDTOMapper {

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


    public TourResponseDTO convertResponse(Tour tour) {
        return TourResponseDTO.builder()
                .id(tour.getId())
                .author(userMapper.convertResponse(tour.getAuthor()))
                .title(tour.getTitle())
                .description(tour.getDescription())
                .isPublic(tour.isPublic())
                .city(cityMapper.convertResponse(tour.getCity()))
                .tags(tour.getTags().stream().map(tagMapper::convertResponse).toList())
                .theme(themeMapper.convertResponse(tour.getTheme()))
                .approxCost(tour.getApproxCost())
                .approxDuration(tour.getApproxDuration())
                .creationDate(tour.getCreationDate())
                .stops(tour.getStops().stream().map(stopMapper::convertResponse).toList())
                .reviews(tour.getReviews().stream().map(reviewMapper::convertResponse).toList())
                .markedAsCompletedCount((long) tour.getMarkedTourists().size())
                .build();
    }

    public List<TourResponseDTO> convertResponse(List<Tour> tour) {
        return tour.stream().map(this::convertResponse).toList();
    }

    public Tour convertCreate(TourCreateDTO t, Long guideAuthorId) {
        Tour tour = Tour.builder()
                .title(t.getTitle())
                .description(t.getDescription())
                .approxCost(t.getApproxCost())
                .approxDuration(t.getApproxDuration())
                .creationDate(new Date())
                .isPublic(t.getIsPublic())
                .author(guideService.findById(guideAuthorId).orElseThrow())
                .stops(t.getStops().stream().map(stopMapper::convertCreate).toList())
                .reports(new ArrayList<>())
                .reviews(new ArrayList<>())
                .city(cityService.findById(t.getCityId()))
                .theme(themeService.findById(t.getThemeId()))
                .tags(t.getTagNames() != null ? tagService.findAllByNames(t.getTagNames()) : new ArrayList<>())
                .sharedTourists(t.getSharedTouristIds() != null ? t.getSharedTouristIds().stream().map(tt -> touristService.findById(tt).orElseThrow()).toList() : new ArrayList<>())
                .markedTourists(new ArrayList<>())
                .build();
        tour.getStops().forEach(s -> s.setTour(tour));
        return tour;
    }

}
