package com.what2see.controller.tour;

import com.what2see.dto.tour.ThemeResponseDTO;
import com.what2see.mapper.tour.ThemeDTOMapper;
import com.what2see.model.user.User;
import com.what2see.service.tour.ThemeService;
import com.what2see.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for theme endpoints
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/theme")
public class ThemeController {

    // dependencies autowired by spring boot

    private final ThemeService themeService;

    private final ThemeDTOMapper themeMapper;

    private final UserService<User> userService;


    /*
     * Some validations are not explicitly performed with try/catch's since RuntimeExceptions are expected to
     * be called and managed from the Spring Boot container in case of failed validation or user not found.
     */

    /**
     * Get the list of available themes
     * @param userId requesting user
     * @return list of available themes as id / name DTO
     */
    @GetMapping()
    public ResponseEntity<List<ThemeResponseDTO>> getAll(@RequestHeader(value="Authentication") Long userId) {
        userService.findById(userId);
        return ResponseEntity.ok(themeService.findAll().stream().map(themeMapper::convertResponse).toList());
    }

}
