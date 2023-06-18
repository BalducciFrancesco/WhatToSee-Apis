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

/**
 * Implementation of the <b>Strategy</b> design pattern.<br>
 * This class is responsible for searching tours based on a generic strategy.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class TourSearch {

    // getters, setters and constructor autowired by lombok

    private final TourSearchStrategy strategy;

    private final TourRepository tourRepository;

    /**
     * Searches and sorts tours based on the generic strategy set as field.
     * @param requesting user that is requesting the search and sort
     * @param city city filter (optional)
     * @param theme theme filter (optional)
     * @param approxDuration maximum duration filter (optional)
     * @param tags tags filter (optional)
     * @return list of sorted tours that match the given criteria
     */
    public List<Tour> searchWithStrategy(User requesting, City city, Theme theme, String approxDuration, List<Tag> tags) {
        List<Tour> result = strategy.executeSearch(tourRepository, requesting, city, theme, approxDuration, tags); // execute the currently selected strategy with the given parameters
        result = strategy.executeSort(result, tourRepository, requesting, city, theme, approxDuration, tags); // sort the result of the search following the currently selected strategy
        return result;
    }

}
