package com.what2see.mapper.tour;

import com.what2see.dto.tour.TagResponseDTO;
import com.what2see.model.tour.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service that converts {@link Tag} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class TagDTOMapper {

    /**
     * Converts a {@link Tag} entity to a {@link TagResponseDTO DTO} that can be sent to client
     * @param tag entity to be converted
     * @return DTO that can be sent to client
     */
    public TagResponseDTO convertResponse(Tag tag) {
        return TagResponseDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
    
}
