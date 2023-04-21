package com.what2see.service.user;

import com.what2see.dto.user.AdministratorLoginDTO;
import com.what2see.model.user.Administrator;
import com.what2see.repository.user.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    public Administrator login(AdministratorLoginDTO dto) {
        return administratorRepository.authenticate(dto.getUsername(), dto.getPassword());
    }
}
