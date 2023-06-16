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

public class TourSearchStrategyGuide implements TourSearchStrategy {

    // look for public tours and those created by current guide
    @Override
    public List<Tour> executeSearch(TourRepository repository, User requesting, City city, Theme theme, String approxDuration, List<Tag> tags) {
        Stream<Tour> result = repository.searchPublicOrCreatedBy(city, theme, approxDuration, (Guide) requesting).stream();
        if(tags != null && !tags.isEmpty()) {
            // filter the tags for those who contain at least one of the requested filters
            result = result.filter(tour -> tour.getTags().stream().anyMatch(tags::contains));
        }
        return result.collect(Collectors.toList());
    }

    // descendant order primarily for reviews count and secondarily for marked count
    // showing first those created by current guide
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
