package com.what2see.exception;

import com.what2see.model.tour.Tour;
import com.what2see.model.user.Tourist;

public class TourNotMarkedException extends RuntimeException {

    private Tour tour;

    private Tourist tourist;

    public TourNotMarkedException(Tour tour, Tourist tourist) {
        this.tour = tour;
        this.tourist = tourist;
    }

    public TourNotMarkedException(String message) {
        super(message);
    }

    public TourNotMarkedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TourNotMarkedException(Throwable cause) {
        super(cause);
    }

    public TourNotMarkedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

}
