package com.what2see.mapper;

import com.what2see.dto.user.GuideLoginResponseDTO;
import com.what2see.dto.user.GuideRegisterDTO;
import com.what2see.dto.user.GuideRegisterResponseDTO;
import com.what2see.model.user.Guide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class GuideDTOMapper {

    public Guide convertRegister(GuideRegisterDTO conv) {
        return Guide.builder()
                .username(conv.getUsername())
                .password(conv.getPassword())
                .firstName(conv.getFirstName())
                .lastName(conv.getLastName())
                .conversations(new ArrayList<>())
                .createdTours(new ArrayList<>())
                .build();
    }

    public GuideLoginResponseDTO convertLoginResponse(Guide loggedGuide) {
        return GuideLoginResponseDTO.builder()
                .id(loggedGuide.getId())
                .username(loggedGuide.getUsername())
                .firstName(loggedGuide.getFirstName())
                .lastName(loggedGuide.getLastName())
                .build();
    }

    public GuideRegisterResponseDTO convertRegisterResponse(Guide createdGuide) {
        return GuideRegisterResponseDTO.builder()
                .id(createdGuide.getId())
                .username(createdGuide.getUsername())
                .firstName(createdGuide.getFirstName())
                .lastName(createdGuide.getLastName())
                .build();
    }

}
