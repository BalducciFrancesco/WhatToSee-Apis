package com.what2see.mapper.user;

import com.what2see.dto.user.MessageCreateDTO;
import com.what2see.dto.user.MessageResponseDTO;
import com.what2see.model.user.Message;
import com.what2see.service.user.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that converts {@link Message} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class MessageDTOMapper {

    // dependencies autowired by spring boot

    private final ConversationService conversationService;

    /**
     * Converts a {@link MessageCreateDTO DTO} to a {@link Message entity} that can be persisted
     * @param m DTO to be converted
     * @param direction direction of the message
     * @return entity that can be persisted
     * @see Message#direction
     */
    public Message convertCreate(MessageCreateDTO m, boolean direction) {
        return Message.builder()
                .content(m.getContent())
                .direction(direction)
                .conversation(conversationService.findById(m.getConversationId()))
                .build();
    }

    /**
     * Converts a {@link Message entity} to a {@link MessageResponseDTO DTO} that can be sent to client
     * @param m entity to be converted
     * @return DTO that can be sent to client
     * @see Message#direction
     */
    public MessageResponseDTO convertResponse(Message m) {
        return MessageResponseDTO.builder()
                .id(m.getId())
                .content(m.getContent())
                .direction(m.getDirection())
                .timestamp(m.getTimestamp())
                .build();
    }

    /**
     * Converts a list of {@link Message entity} to a list of {@link MessageResponseDTO DTO} that can be sent to client
     * @param m list of entities to be converted
     * @return list of DTOs that can be sent to client
     * @see Message#direction
     */
    public List<MessageResponseDTO> convertResponse(List<Message> m) {
        return m.stream().map(this::convertResponse).collect(Collectors.toList());
    }

}
