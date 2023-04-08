package com.what2see.controller.user;

import com.what2see.dto.user.TouristLoginDTO;
import com.what2see.dto.user.TouristLoginResponseDTO;
import com.what2see.dto.user.TouristRegisterDTO;
import com.what2see.dto.user.TouristRegisterResponseDTO;
import com.what2see.mapper.user.TouristDTOMapper;
import com.what2see.model.user.Tourist;
import com.what2see.service.user.TouristService;
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
@RequestMapping("/tourist")
public class TouristController {

    private final TouristService touristService;

    private final TouristDTOMapper touristMapper;

    @PostMapping("/login")
    public ResponseEntity<TouristLoginResponseDTO> login(@RequestBody @Valid TouristLoginDTO t) {
        Tourist loggedTourist = touristService.login(t);
        if(loggedTourist != null) {
            return ResponseEntity.ok(this.touristMapper.convertLoginResponse(loggedTourist));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
    }

    @PostMapping("/register")
    public ResponseEntity<TouristRegisterResponseDTO> register(@RequestBody @Valid TouristRegisterDTO t) {
        try {
            Tourist createdTourist = touristService.register(t);
            return ResponseEntity.ok(this.touristMapper.convertRegisterResponse(createdTourist));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username già esistente");
        }
    }

}
