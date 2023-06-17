package com.what2see.controller.user;

import com.what2see.dto.user.UserLoginDTO;
import com.what2see.dto.user.UserRegisterDTO;
import com.what2see.dto.user.UserResponseDTO;
import com.what2see.dto.user.UserRole;
import com.what2see.mapper.user.GuideDTOMapper;
import com.what2see.mapper.user.TouristDTOMapper;
import com.what2see.mapper.user.UserDTOMapper;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.model.user.User;
import com.what2see.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService<User> userService;

    private final UserService<Tourist> touristService;

    private final UserService<Guide> guideService;

    private final UserDTOMapper userMapper;

    private final TouristDTOMapper touristMapper;

    private final GuideDTOMapper guideMapper;


    @GetMapping("/{roleId}")
    public ResponseEntity<List<UserResponseDTO>> getAll(@PathVariable int roleId) {
        UserRole role = UserRole.values()[roleId];
        Stream<User> response = switch(role) {
            case TOURIST -> touristService.findAll().stream().map(u -> (User) u);
            case GUIDE -> guideService.findAll().stream().map(u -> (User) u);
            case ADMINISTRATOR -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non è possibile conoscere gli amministratori");
        };
        return ResponseEntity.ok(response.map(userMapper::convertResponse).collect(Collectors.toList()));
    }

    // username is trimmed and case-insensitive
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid UserLoginDTO l) {
        User logged = userService.login(l);
        if(logged != null) {
            return ResponseEntity.ok(userMapper.convertResponse(logged));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
    }

    // username is trimmed and case-insensitive
    // first name and last name are trimmed
    @PostMapping("/{roleId}/register")
    public ResponseEntity<UserResponseDTO> register(@PathVariable int roleId, @RequestBody @Valid UserRegisterDTO r) {
        User created;
        UserRole role = UserRole.values()[roleId];
        try {
            created = switch(role) {
                case TOURIST -> touristService.register(touristMapper.convertRegister(r));
                case GUIDE -> guideService.register(guideMapper.convertRegister(r));
                case ADMINISTRATOR -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non è possibile registrare nuovi amministratori");
            };
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username già esistente");
        }
        return ResponseEntity.ok(userMapper.convertResponse(created));
    }

}
