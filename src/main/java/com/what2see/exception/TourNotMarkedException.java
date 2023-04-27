package com.what2see.exception;

public class TourNotMarkedException extends RuntimeException {

    public TourNotMarkedException() {
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

}
