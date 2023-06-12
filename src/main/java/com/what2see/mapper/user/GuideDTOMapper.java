package com.what2see.mapper.user;

import com.what2see.dto.user.UserRegisterDTO;
import com.what2see.model.user.Guide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class GuideDTOMapper {

    public Guide convertRegister(UserRegisterDTO conv) {
        return Guide.builder()
                .username(conv.getUsername().trim().toLowerCase())
                .password(conv.getPassword())
                .firstName(conv.getFirstName().trim())
                .lastName(conv.getLastName().trim())
                .conversations(new ArrayList<>())
                .createdTours(new ArrayList<>())
                .build();
    }

}
