package com.what2see.service.tour.search;

import com.what2see.model.tour.City;
import com.what2see.model.tour.Tag;
import com.what2see.model.tour.Theme;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.User;
import com.what2see.repository.tour.TourRepository;

import java.util.Comparator;
import java.util.List;

public interface TourSearchStrategy {

    List<Tour> executeSearch(TourRepository repository, User requesting, City city, Theme theme, String approxDuration, List<Tag> tags);

    List<Tour> executeSort(List<Tour> toSort, TourRepository repository, User requesting, City city, Theme theme, String approxDuration, List<Tag> tags);

}
