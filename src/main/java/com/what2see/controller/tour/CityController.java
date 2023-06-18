package com.what2see.controller.tour;

import com.what2see.dto.tour.CityResponseDTO;
import com.what2see.mapper.tour.CityDTOMapper;
import com.what2see.model.user.User;
import com.what2see.service.tour.CityService;
import com.what2see.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for city endpoints
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/city")
public class CityController {

    // dependencies autowired by spring boot

    private final CityService cityService;

    private final CityDTOMapper cityMapper;

    private final UserService<User> userService;


    /*
     * Some validations are not explicitly performed with try/catch's since RuntimeExceptions are expected to
     * be called and managed from the Spring Boot container in case of failed validation or user not found.
     */

    /**
     * Get the list of available cities
     * @param userId requesting user
     * @return list of available cities as id / name DTO
     */
    @GetMapping()
    public ResponseEntity<List<CityResponseDTO>> getAll(@RequestHeader(value="Authentication") Long userId) {
        userService.findById(userId);
        return ResponseEntity.ok(cityService.findAll().stream().map(cityMapper::convertResponse).toList());
    }

}
