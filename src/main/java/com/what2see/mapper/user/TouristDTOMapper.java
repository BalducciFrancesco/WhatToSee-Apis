package com.what2see.mapper.user;

import com.what2see.dto.user.UserRegisterDTO;
import com.what2see.model.user.Tourist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service that converts {@link Tourist} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class TouristDTOMapper {

    /**
     * Converts a {@link UserRegisterDTO DTO} to a {@link Tourist entity} that can be persisted
     * @param conv DTO to be converted
     * @return entity that can be persisted
     */
    public Tourist convertRegister(UserRegisterDTO conv) {
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

}
