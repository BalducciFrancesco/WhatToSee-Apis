package com.what2see.mapper.user;

import com.what2see.dto.user.TouristRegisterDTO;
import com.what2see.dto.user.TouristResponseDTO;
import com.what2see.model.user.Tourist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public TouristResponseDTO convertResponse(Tourist t) {
        return TouristResponseDTO.builder()
                .id(t.getId())
                .username(t.getUsername())
                .firstName(t.getFirstName())
                .lastName(t.getLastName())
                .build();
    }

    public List<TouristResponseDTO> convertResponse(List<Tourist> tourists) {
        return tourists.stream().map(this::convertResponse).collect(Collectors.toList());
    }

}
