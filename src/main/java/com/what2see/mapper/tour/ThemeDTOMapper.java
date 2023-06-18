package com.what2see.mapper.tour;

import com.what2see.dto.tour.ThemeResponseDTO;
import com.what2see.model.tour.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service that converts {@link Theme} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class ThemeDTOMapper {

    /**
     * Converts a {@link Theme} entity to a {@link ThemeResponseDTO DTO} that can be sent to client
     * @param theme entity to be converted
     * @return DTO that can be sent to client
     */
    public ThemeResponseDTO convertResponse(Theme theme) {
        return ThemeResponseDTO.builder()
                .id(theme.getId())
                .name(theme.getName())
                .build();
    }
    
}
