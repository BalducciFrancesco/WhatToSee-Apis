package com.what2see.service.user;

import com.what2see.exception.ConversationAlreadyStartedException;
import com.what2see.model.user.Conversation;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.model.user.User;
import com.what2see.repository.user.ConversationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class that handles the business logic for {@link Conversation} entities.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ConversationService {

    // dependencies autowired by spring boot

    private final ConversationRepository conversationRepository;

    /**
     * Returns all {@link Conversation} entities.
     * @return list of all conversations
     */
    public List<Conversation> findAll() {
        return this.conversationRepository.findAll();
    }

    /**
     * Returns the {@link Conversation} entity with the given id.
     * @param conversationId id of the conversation
     * @return conversation with the given id
     * @throws NoSuchElementException if no conversation with the given id exists
     */
    public Conversation findById(Long conversationId) throws NoSuchElementException {
        return this.conversationRepository.findById(conversationId).orElseThrow();
    }

    /**
     * Returns the {@link Conversation} entity with the given participants.
     * @param touristId id of the tourist
     * @param guideId id of the guide
     * @return conversation with the given participants
     * @throws NoSuchElementException if no conversation with the given participants exists
     */
    public Conversation findByParticipants(Long touristId, Long guideId) throws NoSuchElementException {
        return this.conversationRepository.findByTouristIdAndGuideId(touristId, guideId).orElseThrow();
    }

    /**
     * Returns whether the given {@link Conversation} entity is visible to the given {@link User}.<br>
     * A conversation is visible to a user if the user is either the tourist or the guide of the conversation.
     * @param c conversation to check
     * @param userId id of the user
     * @return whether the conversation is visible to the user
     */
    public boolean isVisible(Conversation c, Long userId) {
        return c.getGuide().getId().equals(userId) || c.getTourist().getId().equals(userId);
    }

    /**
     * Create a new {@link Conversation} entity with one or more messages from tourist to guide.<br>
     * Note that doesn't throw error if conversation is started with multiple messages or from guide to tourist.
     * However, this isn't allowed at controller-level.
     * @param newConversation conversation to create
     * @return created conversation
     * @throws ConversationAlreadyStartedException if a conversation with the given participants already exists
     */
    public Conversation startConversation(Conversation newConversation) throws ConversationAlreadyStartedException {
        Tourist creatingTourist = newConversation.getTourist();
        Guide creatingGuide = newConversation.getGuide();

        // check if conversation already exists
        Optional<Conversation> checkExisting = conversationRepository.findByTouristIdAndGuideId(creatingTourist.getId(), creatingGuide.getId());
        if(checkExisting.isPresent())
            throw new ConversationAlreadyStartedException(checkExisting.get());

        return conversationRepository.save(newConversation);
    }

}
