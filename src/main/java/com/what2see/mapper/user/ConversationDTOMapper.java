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

@RequiredArgsConstructor
@Service
public class ConversationDTOMapper {

    private final UserService<Guide> guideService;

    private final UserService<Tourist> touristService;

    private final UserDTOMapper userMapper;

    private final MessageDTOMapper messageMapper;


    public Conversation convertCreate(ConversationCreateDTO c, Long touristId) {
        Conversation conversation = Conversation.builder()
                .guide(guideService.findById(c.getGuideId()))
                .tourist(touristService.findById(touristId))
                .build();
        Message m = Message.builder()
                .content(c.getMessage())
                .direction(false)
                .build();
        m.setConversation(conversation);
        conversation.setMessages(List.of(m));
        return conversation;
    }

    public ConversationResponseDTO convertResponse(Conversation c) {
        return ConversationResponseDTO.builder()
                .id(c.getId())
                .tourist(userMapper.convertResponse(c.getTourist()))
                .guide(userMapper.convertResponse(c.getGuide()))
                .messages(messageMapper.convertResponse(c.getMessages()))
                .build();
    }

    public ConversationResponseDTO convertResponseLight(Conversation c) {
        return ConversationResponseDTO.builder()
                .id(c.getId())
                .tourist(userMapper.convertResponse(c.getTourist()))
                .guide(userMapper.convertResponse(c.getGuide()))
                .build();
    }

    public List<ConversationResponseDTO> convertResponseLight(List<Conversation> c) {
        return c.stream().map(this::convertResponseLight).collect(Collectors.toList());
    }

}
