package com.what2see.mapper.tour;

import com.what2see.dto.tour.TourCreateDTO;
import com.what2see.dto.tour.TourResponseDTO;
import com.what2see.mapper.user.GuideDTOMapper;
import com.what2see.model.tour.Tour;
import com.what2see.repository.tour.CityRepository;
import com.what2see.repository.tour.ThemeRepository;
import com.what2see.repository.user.GuideRepository;
import com.what2see.repository.user.TouristRepository;
import com.what2see.service.tour.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TourDTOMapper {

    private final TouristRepository touristRepository;

    private final GuideDTOMapper guideMapper;

    private final GuideRepository guideRepository;

    private final CityDTOMapper cityMapper;

    private final CityRepository cityRepository;

    private final TagDTOMapper tagMapper;

    private final TagService tagService;


    private final ThemeDTOMapper themeMapper;

    private final ThemeRepository themeRepository;

    private final TourStopDTOMapper tourStopMapper;

    private final ReviewDTOMapper reviewMapper;


    public TourResponseDTO convertResponse(Tour tour) {
        return TourResponseDTO.builder()
                .id(tour.getId())
                .author(guideMapper.convertResponse(tour.getAuthor()))
                .title(tour.getTitle())
                .description(tour.getDescription())
                .isPublic(tour.isPublic())
                .city(cityMapper.convert(tour.getCity()))
                .tags(tour.getTags().stream().map(tagMapper::convert).collect(Collectors.toList()))
                .theme(themeMapper.convert(tour.getTheme()))
                .approxCost(tour.getApproxCost())
                .approxDuration(tour.getApproxDuration())
                .creationDate(tour.getCreationDate())
                .stops(tour.getStops().stream().map(tourStopMapper::convertResponse).collect(Collectors.toList()))
                .reviews(tour.getReviews().stream().map(reviewMapper::convertResponse).collect(Collectors.toList()))
                .build();
    }

    public List<TourResponseDTO> convertResponse(List<Tour> tour) {
        return tour.stream().map(this::convertResponse).collect(Collectors.toList());
    }

    public Tour convertCreate(TourCreateDTO t, Long guideAuthorId) throws NoSuchElementException {
        Tour tour = Tour.builder()
                .title(t.getTitle())
                .description(t.getDescription())
                .approxCost(t.getApproxCost())
                .approxDuration(t.getApproxDuration())
                .creationDate(new Date())
                .isPublic(t.getIsPublic())
                .author(guideRepository.findById(guideAuthorId).orElseThrow())
                .stops(t.getStops().stream().map(tourStopMapper::convertCreate).collect(Collectors.toList()))
                .reports(new ArrayList<>())
                .reviews(new ArrayList<>())
                .city(cityRepository.findById(t.getCityId()).orElseThrow())
                .theme(themeRepository.findById(t.getThemeId()).orElseThrow())
                .tags(t.getTagNames() != null ? tagService.findOrCreateTags(t.getTagNames()) : new ArrayList<>())
                .sharedTourists(t.getSharedTouristIds() != null ? t.getSharedTouristIds().stream().map(tId -> touristRepository.findById(tId).orElseThrow()).collect(Collectors.toList()) : new ArrayList<>())
                .markedTourists(new ArrayList<>())
                .build();
        tour.getStops().forEach(s -> s.setTour(tour));
        return tour;
    }

}
