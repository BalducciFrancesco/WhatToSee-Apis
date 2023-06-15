package com.what2see.controller.tour;

import com.what2see.dto.tour.CityResponseDTO;
import com.what2see.mapper.tour.CityDTOMapper;
import com.what2see.service.tour.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;

    private final CityDTOMapper cityMapper;

    @GetMapping()
    public ResponseEntity<List<CityResponseDTO>> getAll() {
        return ResponseEntity.ok(cityService.findAll().stream().map(cityMapper::convertResponse).collect(Collectors.toList()));
    }

}
