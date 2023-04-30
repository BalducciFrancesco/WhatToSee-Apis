package com.what2see.service.user;

import com.what2see.model.user.Conversation;
import com.what2see.repository.user.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public List<Conversation> getAll() {
        return this.conversationRepository.findAll();
    }

}
