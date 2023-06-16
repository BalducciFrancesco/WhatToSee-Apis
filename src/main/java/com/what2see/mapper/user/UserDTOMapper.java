package com.what2see.mapper.user;

import com.what2see.dto.user.UserResponseDTO;
import com.what2see.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDTOMapper {

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
