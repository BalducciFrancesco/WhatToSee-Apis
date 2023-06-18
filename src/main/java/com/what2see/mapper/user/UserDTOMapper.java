package com.what2see.mapper.user;

import com.what2see.dto.user.UserResponseDTO;
import com.what2see.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service that converts {@link User} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class UserDTOMapper {

    /**
     * Converts a {@link User} entity to a {@link UserResponseDTO DTO} that can be sent to client
     * @param u entity to be converted
     * @return DTO that can be sent to client
     */
    public UserResponseDTO convertResponse(User u) {
        return UserResponseDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .role(UserRoleMapper.mapUserToRole(u).ordinal())
                .build();
    }

}
