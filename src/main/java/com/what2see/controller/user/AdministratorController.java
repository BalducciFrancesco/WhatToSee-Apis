package com.what2see.controller.user;

import com.what2see.dto.user.UserLoginDTO;
import com.what2see.dto.user.UserResponseDTO;
import com.what2see.mapper.user.UserDTOMapper;
import com.what2see.model.user.Administrator;
import com.what2see.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/administrator")
public class AdministratorController {

    private final UserService<Administrator> administratorService;

    private final UserDTOMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid UserLoginDTO t) {
        Administrator loggedAdministrator = administratorService.login(t);
        if(loggedAdministrator != null) {
            return ResponseEntity.ok(userMapper.convertResponse(loggedAdministrator));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
    }

}
