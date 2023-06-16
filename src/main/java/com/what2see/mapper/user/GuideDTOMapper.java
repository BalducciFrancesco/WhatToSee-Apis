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
                .username(conv.getUsername())
                .password(conv.getPassword())
                .firstName(conv.getFirstName())
                .lastName(conv.getLastName())
                .conversations(new ArrayList<>())
                .createdTours(new ArrayList<>())
                .build();
    }

}
