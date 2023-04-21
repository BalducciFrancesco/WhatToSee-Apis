package com.what2see.controller.user;

import com.what2see.dto.user.TouristLoginDTO;
import com.what2see.dto.user.TouristRegisterDTO;
import com.what2see.dto.user.TouristResponseDTO;
import com.what2see.mapper.user.TouristDTOMapper;
import com.what2see.model.user.Tourist;
import com.what2see.service.user.TouristService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tourist")
public class TouristController {

    private final TouristService touristService;

    private final TouristDTOMapper touristMapper;

    @GetMapping()
    public ResponseEntity<List<TouristResponseDTO>> getAll() {
        return ResponseEntity.ok(this.touristService.getAll().stream().map(t -> this.touristMapper.convertResponse(t)).collect(Collectors.toList()));
    }

    @PostMapping("/login")
    public ResponseEntity<TouristResponseDTO> login(@RequestBody @Valid TouristLoginDTO t) {
        Tourist loggedTourist = touristService.login(t);
        if(loggedTourist != null) {
            return ResponseEntity.ok(this.touristMapper.convertResponse(loggedTourist));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
    }

    @PostMapping("/register")
    public ResponseEntity<TouristResponseDTO> register(@RequestBody @Valid TouristRegisterDTO t) {
        try {
            Tourist createdTourist = touristService.register(touristMapper.convertRegister(t));
            return ResponseEntity.ok(this.touristMapper.convertResponse(createdTourist));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username già esistente");
        }
    }

}
