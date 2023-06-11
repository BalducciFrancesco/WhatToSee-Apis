package com.what2see.controller.tour;

import com.what2see.dto.tour.TagResponseDTO;
import com.what2see.mapper.tour.TagDTOMapper;
import com.what2see.service.tour.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    private final TagDTOMapper tagMapper;

    @GetMapping()
    public ResponseEntity<List<TagResponseDTO>> getAll() {
        return ResponseEntity.ok(tagService.findAll().stream().map(tagMapper::convertResponse).collect(Collectors.toList()));
    }

}
