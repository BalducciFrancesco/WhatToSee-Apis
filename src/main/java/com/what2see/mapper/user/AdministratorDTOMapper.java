package com.what2see.mapper.user;

import com.what2see.dto.user.AdministratorResponseDTO;
import com.what2see.model.user.Administrator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdministratorDTOMapper {

    public AdministratorResponseDTO convertResponse(Administrator loggedAdministrator) {
        return AdministratorResponseDTO.builder()
                .id(loggedAdministrator.getId())
                .username(loggedAdministrator.getUsername())
                .firstName(loggedAdministrator.getFirstName())
                .lastName(loggedAdministrator.getLastName())
                .build();
    }

}
