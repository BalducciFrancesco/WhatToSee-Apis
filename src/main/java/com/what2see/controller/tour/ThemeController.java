package com.what2see.controller.tour;

import com.what2see.dto.tour.ThemeResponseDTO;
import com.what2see.mapper.tour.ThemeDTOMapper;
import com.what2see.service.tour.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/theme")
public class ThemeController {

    private final ThemeService themeService;

    private final ThemeDTOMapper themeMapper;

    @GetMapping()
    public ResponseEntity<List<ThemeResponseDTO>> getAll() {
        return ResponseEntity.ok(themeService.findAll().stream().map(themeMapper::convertResponse).collect(Collectors.toList()));
    }

}
