package com.what2see.exception;

public class ConversationAlreadyStartedException extends RuntimeException {

    public ConversationAlreadyStartedException() {
    }

    public ConversationAlreadyStartedException(String message) {
        super(message);
    }

    public ConversationAlreadyStartedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConversationAlreadyStartedException(Throwable cause) {
        super(cause);
    }

    public ConversationAlreadyStartedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
