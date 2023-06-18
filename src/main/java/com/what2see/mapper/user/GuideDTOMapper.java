package com.what2see.mapper.user;

import com.what2see.dto.user.UserRegisterDTO;
import com.what2see.model.user.Guide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service that converts {@link Guide} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class GuideDTOMapper {


    /**
     * Converts a {@link UserRegisterDTO DTO} to a {@link Guide entity} that can be persisted
     * @param conv DTO to be converted
     * @return entity that can be persisted
     */
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
