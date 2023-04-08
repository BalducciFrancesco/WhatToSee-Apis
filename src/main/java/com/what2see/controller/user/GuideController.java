package com.what2see.controller.user;

import com.what2see.dto.user.GuideLoginDTO;
import com.what2see.dto.user.GuideRegisterDTO;
import com.what2see.dto.user.GuideResponseDTO;
import com.what2see.mapper.user.GuideDTOMapper;
import com.what2see.model.user.Guide;
import com.what2see.service.user.GuideService;
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

    private final GuideService guideService;

    private final GuideDTOMapper guideMapper;


    @PostMapping("/login")
    public ResponseEntity<GuideResponseDTO> login(@RequestBody @Valid GuideLoginDTO t) {
        Guide loggedGuide = guideService.login(t);
        if(loggedGuide != null) {
            return ResponseEntity.ok(this.guideMapper.convertResponse(loggedGuide));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
    }

    @PostMapping("/register")
    public ResponseEntity<GuideResponseDTO> register(@RequestBody @Valid GuideRegisterDTO t) {
        try {
            Guide createdGuide = guideService.register(t);
            return ResponseEntity.ok(this.guideMapper.convertResponse(createdGuide));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username già esistente");
        }
    }

}
