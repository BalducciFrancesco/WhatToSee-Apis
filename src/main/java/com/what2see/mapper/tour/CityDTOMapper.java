package com.what2see.mapper.tour;

import com.what2see.dto.tour.CityResponseDTO;
import com.what2see.model.tour.City;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CityDTOMapper {

    public CityResponseDTO convertResponse(City city) {
        return CityResponseDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }
    
}
