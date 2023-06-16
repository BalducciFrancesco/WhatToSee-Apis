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

@RequiredArgsConstructor
@RestController
@RequestMapping("/theme")
public class ThemeController {

    private final ThemeService themeService;

    private final ThemeDTOMapper themeMapper;

    private final UserService<User> userService;

    @GetMapping()
    public ResponseEntity<List<ThemeResponseDTO>> getAll(@RequestHeader(value="Authentication") Long userId) {
        userService.findById(userId);
        return ResponseEntity.ok(themeService.findAll().stream().map(themeMapper::convertResponse).toList());
    }

}
