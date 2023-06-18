package com.what2see.service.tour.search;

import com.what2see.model.tour.City;
import com.what2see.model.tour.Tag;
import com.what2see.model.tour.Theme;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Guide;
import com.what2see.model.user.User;
import com.what2see.repository.tour.TourRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of the <b>Strategy</b> design pattern.<br>
 * This class represents a concrete strategy for searching tours that is suitable for <b>Guides</b>.<br>
 * This strategy can look for <b>public and private (only if created by the current guide)</b> tours and sort them by <i>descending</i> order based on the following criteria (order of precedence):
 * <ol><li>
 *     created by current guide
 * </li><li>
 *     reviews count
 * </li><li>
 *     marked count
 * </li></ol>
 */
public class TourSearchStrategyGuide implements TourSearchStrategy {

    /**
     * Searches tours based on the given parameters among public tours and those private that are created by the current guide
     * @param repository tour repository (should be autowired in the client class)
     * @param requesting guide that is requesting the search
     * @param city city filter (optional)
     * @param theme theme filter (optional)
     * @param approxDuration maximum duration filter (optional)
     * @param tags tags filter (optional)
     * @return list of tours that match the given criteria
     */
    @Override
    public List<Tour> executeSearch(TourRepository repository, User requesting, City city, Theme theme, String approxDuration, List<Tag> tags) {
        Stream<Tour> result = repository.searchPublicOrCreatedBy(city, theme, approxDuration, (Guide) requesting).stream(); // get all public tours and those private that are created by the current guide
        if(tags != null && !tags.isEmpty()) {
            // filter the tours for those who contain at least one of the requested tags
            result = result.filter(tour -> tour.getTags().stream().anyMatch(tags::contains));
        }
        return result.collect(Collectors.toList());
    }

    /**
     * Sorts the given list of tours by <i>descending</i> order based on the following criteria (order of precedence):
     * <ol><li>
     *     created by current guide
     * </li><li>
     *     reviews count
     * </li><li>
     *     marked count
     * </li></ol>
     * @param toSort list of tours to sort
     * @param repository tour repository (should be autowired in the client class)
     * @param requesting guide that is requesting the ordering
     * @param city city filter (optional)
     * @param theme theme filter (optional)
     * @param approxDuration maximum duration filter (optional)
     * @param tags tags filter (optional)
     * @return sorted list of tours passed as first parameter
     */
    @Override
    public List<Tour> executeSort(List<Tour> toSort, TourRepository repository, User requesting, City city, Theme theme, String approxDuration, List<Tag> tags) {
        List<Tour> sorted = new ArrayList<>(toSort);
        sorted.sort((Tour t1, Tour t2) -> {
            if((t1.getAuthor() == requesting || t2.getAuthor() == requesting) && t1.getAuthor() != t2.getAuthor()) {
                // only one of them has current guide as author, show that first
                return t1.getAuthor() == requesting ? -1 : 1;
            } else {
                // both or none of them has current guide as author, sort like tourist
                int reviewComp = t2.getReviews().size() - t1.getReviews().size();
                return reviewComp != 0 ? reviewComp : t2.getMarkedTourists().size() - t1.getMarkedTourists().size();
            }
        });
        return sorted;
    }

}
