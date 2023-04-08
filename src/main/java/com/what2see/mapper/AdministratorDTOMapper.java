package com.what2see.mapper;

import com.what2see.dto.user.AdministratorLoginResponseDTO;
import com.what2see.model.user.Administrator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdministratorDTOMapper {

    public AdministratorLoginResponseDTO convertLoginResponse(Administrator loggedAdministrator) {
        return AdministratorLoginResponseDTO.builder()
                .id(loggedAdministrator.getId())
                .username(loggedAdministrator.getUsername())
                .firstName(loggedAdministrator.getFirstName())
                .lastName(loggedAdministrator.getLastName())
                .build();
    }

}
