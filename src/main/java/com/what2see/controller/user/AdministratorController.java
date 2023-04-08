package com.what2see.controller.user;

import com.what2see.dto.user.AdministratorLoginDTO;
import com.what2see.dto.user.AdministratorResponseDTO;
import com.what2see.mapper.user.AdministratorDTOMapper;
import com.what2see.model.user.Administrator;
import com.what2see.service.user.AdministratorService;
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

    private final AdministratorService administratorService;

    private final AdministratorDTOMapper administratorMapper;

    @PostMapping("/login")
    public ResponseEntity<AdministratorResponseDTO> login(@RequestBody @Valid AdministratorLoginDTO t) {
        Administrator loggedAdministrator = administratorService.login(t);
        if(loggedAdministrator != null) {
            return ResponseEntity.ok(this.administratorMapper.convertLoginResponse(loggedAdministrator));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
    }

}
