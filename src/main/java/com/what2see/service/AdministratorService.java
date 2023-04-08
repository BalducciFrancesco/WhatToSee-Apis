package com.what2see.service;

import com.what2see.dto.user.AdministratorLoginDTO;
import com.what2see.model.user.Administrator;
import com.what2see.repository.AdministratorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    public Administrator login(AdministratorLoginDTO dto) {
        Administrator t = administratorRepository.authenticate(dto.getUsername(), dto.getPassword());
        return t;
    }
}
