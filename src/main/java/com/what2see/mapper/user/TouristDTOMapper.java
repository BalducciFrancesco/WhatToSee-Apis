package com.what2see.mapper.user;

import com.what2see.dto.user.UserRegisterDTO;
import com.what2see.model.user.Tourist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class TouristDTOMapper {

    public Tourist convertRegister(UserRegisterDTO conv) {
        return Tourist.builder()
                .username(conv.getUsername().trim().toLowerCase())
                .password(conv.getPassword())
                .firstName(conv.getFirstName().trim())
                .lastName(conv.getLastName().trim())
                .conversations(new ArrayList<>())
                .markedTours(new ArrayList<>())
                .reviewedTours(new ArrayList<>())
                .reportedTours(new ArrayList<>())
                .sharedTours(new ArrayList<>())
                .build();
    }

}
