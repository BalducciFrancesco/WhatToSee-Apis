package com.what2see.service.user;

import com.what2see.dto.user.TouristLoginDTO;
import com.what2see.model.user.Tourist;
import com.what2see.repository.user.TouristRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TouristService {

    private final TouristRepository touristRepository;

    public List<Tourist> getAll() {
        return this.touristRepository.findAll();
    }

    public Tourist register(Tourist t) throws DataIntegrityViolationException {
        return touristRepository.save(t);
    }

    public Tourist login(TouristLoginDTO dto) {
        return touristRepository.authenticate(dto.getUsername(), dto.getPassword());
    }

    public Tourist findById(Long touristId) throws NoSuchElementException {
        return touristRepository.findById(touristId).orElseThrow();
    }

}
