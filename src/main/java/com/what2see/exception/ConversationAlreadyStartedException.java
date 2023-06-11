package com.what2see.exception;

import com.what2see.model.user.Conversation;

public class ConversationAlreadyStartedException extends RuntimeException {

    private Conversation existingConversation;

    public ConversationAlreadyStartedException(Conversation existingConversation) {
        this.existingConversation = existingConversation;
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

    public Conversation getExistingConversation() {
        return existingConversation;
    }

    public void setExistingConversation(Conversation existingConversation) {
        this.existingConversation = existingConversation;
    }

}
