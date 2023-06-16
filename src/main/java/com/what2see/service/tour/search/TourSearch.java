package com.what2see.service.tour.search;

import com.what2see.model.tour.City;
import com.what2see.model.tour.Tag;
import com.what2see.model.tour.Theme;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.User;
import com.what2see.repository.tour.TourRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class TourSearch {

    private final TourSearchStrategy strategy;

    private final TourRepository tourRepository;

    public List<Tour> searchWithStrategy(User requesting, City city, Theme theme, String approxDuration, List<Tag> tags) {
        List<Tour> result = strategy.executeSearch(tourRepository, requesting, city, theme, approxDuration, tags);
        result = strategy.executeSort(result, tourRepository, requesting, city, theme, approxDuration, tags);
        return result;
    }

}
