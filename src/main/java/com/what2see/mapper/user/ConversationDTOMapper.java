package com.what2see.mapper.user;

import com.what2see.dto.user.ConversationCreateDTO;
import com.what2see.dto.user.ConversationResponseDTO;
import com.what2see.dto.user.MessageCreateDTO;
import com.what2see.model.user.Conversation;
import com.what2see.model.user.Message;
import com.what2see.service.user.GuideService;
import com.what2see.service.user.TouristService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ConversationDTOMapper {

    private final GuideService guideService;

    private final TouristService touristService;

    private final GuideDTOMapper guideMapper;

    private final TouristDTOMapper touristMapper;

    private final MessageDTOMapper messageMapper;


    public Conversation convertCreate(ConversationCreateDTO c) {
        Conversation conversation = Conversation.builder()
                .guide(guideService.findById(c.getGuideId()))
                .tourist(touristService.findById(c.getTouristId()))
                .build();
        Message m = messageMapper.convertCreate(MessageCreateDTO.builder()
                .content(c.getMessage())
                .direction(false)
                .build());
        m.setConversation(conversation);
        conversation.setMessages(List.of(m));
        return conversation;
    }

    public ConversationResponseDTO convertResponse(Conversation c) {
        return ConversationResponseDTO.builder()
                .id(c.getId())
                .tourist(touristMapper.convertResponse(c.getTourist()))
                .guide(guideMapper.convertResponse(c.getGuide()))
                .messages(messageMapper.convertResponse(c.getMessages()))
                .build();
    }

    public ConversationResponseDTO convertResponseLight(Conversation c) {
        return ConversationResponseDTO.builder()
                .id(c.getId())
                .tourist(touristMapper.convertResponse(c.getTourist()))
                .guide(guideMapper.convertResponse(c.getGuide()))
                .build();
    }

    public List<ConversationResponseDTO> convertResponseLight(List<Conversation> c) {
        return c.stream().map(this::convertResponseLight).collect(Collectors.toList());
    }

}
