package com.what2see.service.tour.search;

import com.what2see.model.tour.City;
import com.what2see.model.tour.Tag;
import com.what2see.model.tour.Theme;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.User;
import com.what2see.repository.tour.TourRepository;

import java.util.List;

/**
 * Implementation of the <b>Strategy</b> design pattern.<br>
 * This interface generalizes a search strategy for tours.
 */
public interface TourSearchStrategy {

    /**
     * Executes a search for tours based on the given parameters.<br>
     * Concrete strategy must be provided by the implementing class.
     * @param repository tour repository (should be autowired in the client class)
     * @param requesting user that is requesting the search
     * @param city city filter (optional)
     * @param theme theme filter (optional)
     * @param approxDuration maximum duration filter (optional)
     * @param tags tags filter (optional)
     * @return list of tours that match the given criteria
     */
    List<Tour> executeSearch(TourRepository repository, User requesting, City city, Theme theme, String approxDuration, List<Tag> tags);

    /**
     * Executes a sort for tours based on the given parameters.<br>
     * Usually called on the result of {@link #executeSearch}.<br>
     * Concrete strategy must be provided by the implementing class.
     * @param toSort list of tours to sort
     * @param repository tour repository (should be autowired in the client class)
     * @param requesting user that is requesting the ordering
     * @param city city filter (optional)
     * @param theme theme filter (optional)
     * @param approxDuration maximum duration filter (optional)
     * @param tags tags filter (optional)
     * @return sorted list of tours passed as first parameter
     */
    List<Tour> executeSort(List<Tour> toSort, TourRepository repository, User requesting, City city, Theme theme, String approxDuration, List<Tag> tags);

}
