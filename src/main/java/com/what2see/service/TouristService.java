package com.what2see.service;

import com.what2see.dto.user.TouristLoginDTO;
import com.what2see.dto.user.TouristRegisterDTO;
import com.what2see.mapper.TouristDTOMapper;
import com.what2see.model.user.Tourist;
import com.what2see.repository.TouristRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TouristService {

    private final TouristRepository touristRepository;
    private final TouristDTOMapper touristMapper;

    public Tourist register(TouristRegisterDTO dto) {
        Tourist t = touristMapper.convertRegister(dto);
        return touristRepository.save(t);
    }

    public Tourist login(TouristLoginDTO dto) {
        Tourist t = touristRepository.authenticate(dto.getUsername(), dto.getPassword());
        return t;
    }
}
