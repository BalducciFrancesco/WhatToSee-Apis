package com.what2see.service.user;

import com.what2see.model.user.Message;
import com.what2see.repository.user.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class that handles the business logic for {@link Message} entities.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    // dependencies autowired by spring boot

    private final MessageRepository messageRepository;

    /**
     * Creates a new {@link Message} entity.
     * @param m message to be created (without id)
     * @return created message (with id)
     */
    public Message sendMessage(Message m) {
        return messageRepository.save(m);
    }
}
