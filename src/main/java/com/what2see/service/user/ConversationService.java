package com.what2see.service.user;

import com.what2see.exception.ConversationAlreadyStartedException;
import com.what2see.model.user.Conversation;
import com.what2see.repository.user.ConversationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;


    public List<Conversation> getAll() {
        return this.conversationRepository.findAll();
    }

    public Conversation findById(Long conversationId) {
        return this.conversationRepository.findById(conversationId).orElseThrow();
    }

    public Conversation findByParticipants(Long touristId, Long guideId) {
        return this.conversationRepository.findByTouristIdAndGuideId(touristId, guideId);   // can be null if not existing yet
    }

    public boolean checkVisibility(Conversation c, Long userId) {
        return c.getGuide().getId().equals(userId) || c.getTourist().getId().equals(userId);
    }

    // tries to create a new conversation with an initial message from tourist to guide
    public Conversation startConversation(Conversation c) throws ConversationAlreadyStartedException {
        if(conversationRepository.findByTouristIdAndGuideId(c.getTourist().getId(), c.getGuide().getId()) != null)
            throw new ConversationAlreadyStartedException();    // TODO add indication of existing id for more lightweight queries
        return conversationRepository.save(c);
    }

}
