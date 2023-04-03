package com.what2see.mapper;

import com.what2see.dto.user.TouristLoginResponseDTO;
import com.what2see.dto.user.TouristRegisterDTO;
import com.what2see.dto.user.TouristRegisterResponseDTO;
import com.what2see.model.user.Tourist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class TouristDTOMapper {

    public Tourist convertRegister(TouristRegisterDTO conv) {
        return Tourist.builder()
                .username(conv.getUsername())
                .password(conv.getPassword())
                .firstName(conv.getFirstName())
                .lastName(conv.getLastName())
                .conversations(new ArrayList<>())
                .markedTours(new ArrayList<>())
                .reviewedTours(new ArrayList<>())
                .reportedTours(new ArrayList<>())
                .sharedTours(new ArrayList<>())
                .build();
    }

    public TouristLoginResponseDTO convertLoginResponse(Tourist loggedTourist) {
        return TouristLoginResponseDTO.builder()
                .id(loggedTourist.getId())
                .username(loggedTourist.getUsername())
                .firstName(loggedTourist.getFirstName())
                .lastName(loggedTourist.getLastName())
                .build();
    }

    public TouristRegisterResponseDTO convertRegisterResponse(Tourist createdTourist) {
        return TouristRegisterResponseDTO.builder()
                .id(createdTourist.getId())
                .username(createdTourist.getUsername())
                .firstName(createdTourist.getFirstName())
                .lastName(createdTourist.getLastName())
                .build();
    }
}
