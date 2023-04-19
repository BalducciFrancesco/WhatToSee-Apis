package com.what2see.service.tour;

import com.what2see.dto.tour.TourCreateDTO;
import com.what2see.dto.tour.TourSearchDTO;
import com.what2see.mapper.tour.TourDTOMapper;
import com.what2see.model.tour.City;
import com.what2see.model.tour.Tag;
import com.what2see.model.tour.Theme;
import com.what2see.model.tour.Tour;
import com.what2see.repository.tour.CityRepository;
import com.what2see.repository.tour.TagRepository;
import com.what2see.repository.tour.ThemeRepository;
import com.what2see.repository.tour.TourRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TourService {

    private final TourRepository tourRepository;
    private final TourDTOMapper tourMapper;

    private final CityRepository cityRepository;

    private final TagRepository tagRepository;

    private final ThemeRepository themeRepository;


    public Tour create(TourCreateDTO dto, Long guideId) {
        Tour t = tourMapper.convertCreate(dto, guideId);
        return tourRepository.save(t);
    }

    public List<Tour> search(TourSearchDTO s) throws NoSuchElementException {
        // if search field ids are specified, they must match an existing entity
        City city = s.getCityId() != null ? cityRepository.findById(s.getCityId()).orElseThrow() : null;
        Theme theme = s.getThemeId() != null ? themeRepository.findById(s.getThemeId()).orElseThrow() : null;
        List<Tag> tags = s.getTagIds() != null ? tagRepository.findAllById(s.getTagIds()) : null;

        List<Tour> result = tourRepository.search(city, theme, s.getApproxDuration());
        if(tags != null && !tags.isEmpty())
            // filter the tags for those who contain at least one of the requested filters
            result = result.stream().filter(tour -> tour.getTags().stream().anyMatch(tags::contains)).collect(Collectors.toList());

        return result;
    }
}
