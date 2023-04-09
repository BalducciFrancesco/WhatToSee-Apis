package com.what2see.mapper.tour;

import com.what2see.dto.tour.ThemeResponseDTO;
import com.what2see.model.tour.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ThemeDTOMapper {

    public ThemeResponseDTO convert(Theme theme) {
        return ThemeResponseDTO.builder()
                .id(theme.getId())
                .name(theme.getName())
                .build();
    }
    
}
