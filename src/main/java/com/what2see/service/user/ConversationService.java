package com.what2see.service.user;

import com.what2see.exception.ConversationAlreadyStartedException;
import com.what2see.model.user.Conversation;
import com.what2see.repository.user.ConversationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;


    public List<Conversation> findAll() {
        return this.conversationRepository.findAll();
    }

    public Conversation findById(Long conversationId) {
        return this.conversationRepository.findById(conversationId).orElseThrow();
    }

    public Conversation findByParticipants(Long touristId, Long guideId) {
        return this.conversationRepository.findByTouristIdAndGuideId(touristId, guideId).orElseThrow();   // can be null if not existing yet
    }

    public boolean isVisible(Conversation c, Long userId) {
        return c.getGuide().getId().equals(userId) || c.getTourist().getId().equals(userId);
    }

    // tries to create a new conversation with an initial message from tourist to guide
    // this method doesn't throw error if conversation is started with multiple messages or from guide to tourist
    public Conversation startConversation(Conversation newConversation) throws ConversationAlreadyStartedException {
        Optional<Conversation> checkExisting = conversationRepository.findByTouristIdAndGuideId(newConversation.getTourist().getId(), newConversation.getGuide().getId());
        if(checkExisting.isPresent())
            throw new ConversationAlreadyStartedException(checkExisting.get());
        return conversationRepository.save(newConversation);
    }

}
