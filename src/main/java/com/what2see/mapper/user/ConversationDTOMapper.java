package com.what2see.mapper.user;

import com.what2see.dto.user.ConversationCreateDTO;
import com.what2see.dto.user.ConversationResponseDTO;
import com.what2see.model.user.Conversation;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Message;
import com.what2see.model.user.Tourist;
import com.what2see.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that converts {@link Conversation} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class ConversationDTOMapper {

    // dependencies autowired by spring boot

    private final UserService<Guide> guideService;

    private final UserService<Tourist> touristService;

    private final UserDTOMapper userMapper;

    private final MessageDTOMapper messageMapper;


    /**
     * Converts a {@link ConversationCreateDTO DTO} to a {@link Conversation entity} that can be persisted
     * @param c DTO to be converted
     * @param touristId id of the tourist that is creating the conversation
     * @return entity that can be persisted
     */
    public Conversation convertCreate(ConversationCreateDTO c, Long touristId) {
        Message m = Message.builder()
                .content(c.getMessage())
                .direction(false)
                .build();
        Conversation conversation = Conversation.builder()
                .guide(guideService.findById(c.getGuideId()))
                .tourist(touristService.findById(touristId))
                .messages(List.of(m))
                .build();
        m.setConversation(conversation);    // important because of single-side relation ownership
        return conversation;
    }

    /**
     * Converts a {@link Conversation entity} to a {@link ConversationResponseDTO DTO} that can be sent to client
     * @param c entity to be converted
     * @return DTO that can be sent to client
     */
    public ConversationResponseDTO convertResponse(Conversation c) {
        return ConversationResponseDTO.builder()
                .id(c.getId())
                .tourist(userMapper.convertResponse(c.getTourist()))
                .guide(userMapper.convertResponse(c.getGuide()))
                .messages(messageMapper.convertResponse(c.getMessages()))
                .build();
    }

    /**
     * Converts a {@link Conversation entity} to a {@link ConversationResponseDTO DTO} that can be sent to client.<br>
     * Note that this DTO does not contain the messages of the conversation. Can be used to reduce the amount of data sent to client.
     * @param c entity to be converted
     * @return DTO that can be sent to client
     */
    public ConversationResponseDTO convertResponseLight(Conversation c) {
        return ConversationResponseDTO.builder()
                .id(c.getId())
                .tourist(userMapper.convertResponse(c.getTourist()))
                .guide(userMapper.convertResponse(c.getGuide()))
                .build();
    }

    /**
     * Converts a list of {@link Conversation entities} to a list of {@link ConversationResponseDTO DTOs} that can be sent to client.<br>
     * Note that these DTOs do not contain the messages of the conversations. Can be used to reduce the amount of data sent to client.
     * @param c list of entities to be converted
     * @return list of DTOs that can be sent to client
     */
    public List<ConversationResponseDTO> convertResponseLight(List<Conversation> c) {
        return c.stream().map(this::convertResponseLight).collect(Collectors.toList());
    }

}
