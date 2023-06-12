package com.what2see.controller.user;

import com.what2see.dto.user.UserLoginDTO;
import com.what2see.dto.user.UserRegisterDTO;
import com.what2see.dto.user.UserResponseDTO;
import com.what2see.mapper.user.TouristDTOMapper;
import com.what2see.mapper.user.UserDTOMapper;
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

    private final UserDTOMapper userMapper;

    private final TouristDTOMapper touristMapper;


    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(this.touristService.getAll().stream().map(userMapper::convertResponse).collect(Collectors.toList()));
    }

    // username is trimmed and case-insensitive
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid UserLoginDTO t) {
        Tourist loggedTourist = touristService.login(t);
        if(loggedTourist != null) {
            return ResponseEntity.ok(userMapper.convertResponse(loggedTourist));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
    }

    // username is trimmed and case-insensitive
    // first name and last name are trimmed
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegisterDTO t) {
        try {
            Tourist createdTourist = touristService.register(touristMapper.convertRegister(t));
            return ResponseEntity.ok(userMapper.convertResponse(createdTourist));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username già esistente");
        }
    }

}
