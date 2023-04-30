package com.what2see.mapper.user;

import com.what2see.dto.user.ConversationResponseDTO;
import com.what2see.model.user.Conversation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ConversationDTOMapper {

    private final GuideDTOMapper guideMapper;

    private final TouristDTOMapper touristMapper;

    public ConversationResponseDTO convertResponse(Conversation c) {
        return ConversationResponseDTO.builder()
                .id(c.getId())
                .tourist(touristMapper.convertResponse(c.getTourist()))
                .guide(guideMapper.convertResponse(c.getGuide()))
                .messages(c.getMessages())
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
