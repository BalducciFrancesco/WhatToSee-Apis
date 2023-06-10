package com.what2see.controller.user;

import com.what2see.dto.user.UserLoginDTO;
import com.what2see.dto.user.UserRegisterDTO;
import com.what2see.dto.user.UserResponseDTO;
import com.what2see.mapper.user.GuideDTOMapper;
import com.what2see.mapper.user.UserDTOMapper;
import com.what2see.model.user.Guide;
import com.what2see.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/guide")
public class GuideController {

    private final UserService<Guide> guideService;

    private final UserDTOMapper userMapper;

    private final GuideDTOMapper guideMapper;


    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid UserLoginDTO g) {
        Guide loggedGuide = guideService.login(g);
        if(loggedGuide != null) {
            return ResponseEntity.ok(userMapper.convertResponse(loggedGuide));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegisterDTO g) {
        try {
            Guide createdGuide = guideService.register(guideMapper.convertRegister(g));
            return ResponseEntity.ok(userMapper.convertResponse(createdGuide));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username già esistente");
        }
    }

}
