package com.what2see.mapper.tour;

import com.what2see.dto.tour.TagResponseDTO;
import com.what2see.model.tour.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TagDTOMapper {

    public TagResponseDTO convertResponse(Tag tag) {
        return TagResponseDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
    
}
