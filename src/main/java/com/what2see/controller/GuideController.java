package com.what2see.controller;

import com.what2see.dto.user.GuideLoginDTO;
import com.what2see.dto.user.GuideLoginResponseDTO;
import com.what2see.dto.user.GuideRegisterDTO;
import com.what2see.dto.user.GuideRegisterResponseDTO;
import com.what2see.mapper.GuideDTOMapper;
import com.what2see.model.user.Guide;
import com.what2see.service.GuideService;
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
    public ResponseEntity<GuideLoginResponseDTO> login(@RequestBody @Valid GuideLoginDTO t) {
        Guide loggedGuide = guideService.login(t);
        if(loggedGuide != null) {
            return ResponseEntity.ok(this.guideMapper.convertLoginResponse(loggedGuide));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
    }

    @PostMapping("/register")
    public ResponseEntity<GuideRegisterResponseDTO> register(@RequestBody @Valid GuideRegisterDTO t) {
        try {
            Guide createdGuide = guideService.register(t);
            return ResponseEntity.ok(this.guideMapper.convertRegisterResponse(createdGuide));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username già esistente");
        }
    }

}
