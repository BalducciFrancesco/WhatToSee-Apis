package com.what2see.utils;

import com.what2see.model.tour.Tour;

import java.util.Comparator;

public class TourSearchResultComparator implements Comparator<Tour> {

    // descendant order primarily for reviews count and secondarily for marked count
    @Override
    public int compare(Tour t1, Tour t2) {
        int reviewComp = t2.getReviews().size() - t1.getReviews().size();
        return reviewComp != 0 ? reviewComp : t2.getMarkedTourists().size() - t1.getMarkedTourists().size();
    }
}
