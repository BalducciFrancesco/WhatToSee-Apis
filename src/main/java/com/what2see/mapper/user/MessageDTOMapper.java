package com.what2see.mapper.user;

import com.what2see.dto.user.MessageResponseDTO;
import com.what2see.model.user.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MessageDTOMapper {

    public MessageResponseDTO convertResponse(Message m) {
        return MessageResponseDTO.builder()
                .id(m.getId())
                .content(m.getContent())
                .direction(m.getDirection())
                .timestamp(m.getTimestamp())
                .build();
    }

    public List<MessageResponseDTO> convertResponse(List<Message> m) {
        return m.stream().map(this::convertResponse).collect(Collectors.toList());
    }

}
