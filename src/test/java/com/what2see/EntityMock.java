package com.what2see;

import com.what2see.model.tour.*;
import com.what2see.model.user.Administrator;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.repository.tour.*;
import com.what2see.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EntityMock {

    private final CityRepository cityRepository;

    private final TagRepository tagRepository;

    private final ThemeRepository themeRepository;

    private final ReportRepository reportRepository;

    private final TourRepository tourRepository;


    private final UserRepository<Tourist> touristRepository;

    private final UserRepository<Guide> guideRepository;

    private final UserRepository<Administrator> administratorRepository;

    // -----------
    // TOUR
    // -----------

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCity() {
        return cityRepository.findById(1L).orElseThrow();
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTag() {
        return tagRepository.findById(1L).orElseThrow();
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    public Theme getTheme() {
        return themeRepository.findById(1L).orElseThrow();
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Tour getTour() {
        Tour t = tourRepository.findById(1L).orElseThrow();
        t.getStops().size();
        t.getSharedTourists().size();
        t.getReviews().size();
        return t;
    }

    public Report getReport() {
        return reportRepository.findById(1L).orElseThrow();
    }

    // -----------
    // USER
    // -----------

    public List<Tourist> getAllTourists() {
        return touristRepository.findAll();
    }

    public Tourist getTourist() {
        return touristRepository.findById(1L).orElseThrow();
    }

    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    public Guide getGuide() {
        return guideRepository.findById(3L).orElseThrow();
    }

    public Administrator getAdministrator() {
        return administratorRepository.findById(5L).orElseThrow();
    }

}
