package com.what2see.exception;

import com.what2see.model.tour.Tour;
import com.what2see.model.user.Tourist;

/**
 * Exception thrown when a user tries to interact with a tour when that interaction has already been performed and isn't expected to be repeatable.
 * (e.g. tourist tries to mark / review / report a tour multiple times)
 * @see com.what2see.service.tour.ReviewService#create
 */
public class InteractionAlreadyPerformedException extends RuntimeException {

    private Tour tour;

    private Tourist tourist;

    public InteractionAlreadyPerformedException(Tour tour, Tourist tourist) {
        this.tour = tour;
        this.tourist = tourist;
    }

    public InteractionAlreadyPerformedException(String message) {
        super(message);
    }

    public InteractionAlreadyPerformedException(String message, Throwable cause) {
        super(message, cause);
    }

    public InteractionAlreadyPerformedException(Throwable cause) {
        super(cause);
    }

    public InteractionAlreadyPerformedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
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
