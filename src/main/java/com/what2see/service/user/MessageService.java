package com.what2see.service.user;

import com.what2see.model.user.Message;
import com.what2see.repository.user.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;


    public Message sendMessage(Message m) {
        return messageRepository.save(m);
    }
}