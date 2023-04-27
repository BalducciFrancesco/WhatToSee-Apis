package com.what2see.service.user;

import com.what2see.dto.user.GuideLoginDTO;
import com.what2see.model.user.Guide;
import com.what2see.repository.user.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final GuideRepository guideRepository;

    public Guide register(Guide g) throws DataIntegrityViolationException {
        return guideRepository.save(g);
    }

    public Guide login(GuideLoginDTO dto) {
        return guideRepository.authenticate(dto.getUsername(), dto.getPassword());
    }

    public Guide findById(Long guideId) throws NoSuchElementException {
        return guideRepository.findById(guideId).orElseThrow();
    }
}
