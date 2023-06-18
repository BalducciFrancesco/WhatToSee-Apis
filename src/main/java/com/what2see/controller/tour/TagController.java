package com.what2see.controller.tour;

import com.what2see.dto.tour.TagResponseDTO;
import com.what2see.mapper.tour.TagDTOMapper;
import com.what2see.model.user.User;
import com.what2see.service.tour.TagService;
import com.what2see.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for tag endpoints
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/tag")
public class TagController {

    // dependencies autowired by spring boot

    private final TagService tagService;

    private final TagDTOMapper tagMapper;

    private final UserService<User> userService;


    /*
     * Some validations are not explicitly performed with try/catch's since RuntimeExceptions are expected to
     * be called and managed from the Spring Boot container in case of failed validation or user not found.
     */

    /**
     * Get the list of available tags
     * @param userId requesting user
     * @return list of available tags as id / name DTO
     */
    @GetMapping()
    public ResponseEntity<List<TagResponseDTO>> getAll(@RequestHeader(value="Authentication") Long userId) {
        userService.findById(userId);
        return ResponseEntity.ok(tagService.findAll().stream().map(tagMapper::convertResponse).toList());
    }

}
