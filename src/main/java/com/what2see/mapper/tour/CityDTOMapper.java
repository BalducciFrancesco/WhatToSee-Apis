package com.what2see.mapper.tour;

import com.what2see.dto.tour.CityResponseDTO;
import com.what2see.model.tour.City;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service that converts {@link City} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class CityDTOMapper {

    /**
     * Converts a {@link City} entity to a {@link CityResponseDTO DTO} that can be sent to client
     * @param city entity to be converted
     * @return DTO that can be sent to client
     */
    public CityResponseDTO convertResponse(City city) {
        return CityResponseDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }
    
}
