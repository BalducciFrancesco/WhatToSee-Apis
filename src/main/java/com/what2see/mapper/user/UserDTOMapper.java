package com.what2see.mapper.user;

import com.what2see.dto.user.UserResponseDTO;
import com.what2see.dto.user.UserRole;
import com.what2see.model.user.Administrator;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDTOMapper {

    public UserResponseDTO convertResponse(Tourist t) {
        return UserResponseDTO.builder()
                .id(t.getId())
                .username(t.getUsername())
                .firstName(t.getFirstName())
                .lastName(t.getLastName())
                .role(UserRole.TOURIST.ordinal())
                .build();
    }

    public UserResponseDTO convertResponse(Guide g) {
        return UserResponseDTO.builder()
                .id(g.getId())
                .username(g.getUsername())
                .firstName(g.getFirstName())
                .lastName(g.getLastName())
                .role(UserRole.GUIDE.ordinal())
                .build();
    }

    public UserResponseDTO convertResponse(Administrator a) {
        return UserResponseDTO.builder()
                .id(a.getId())
                .username(a.getUsername())
                .firstName(a.getFirstName())
                .lastName(a.getLastName())
                .role(UserRole.ADMINISTRATOR.ordinal())
                .build();
    }

}
