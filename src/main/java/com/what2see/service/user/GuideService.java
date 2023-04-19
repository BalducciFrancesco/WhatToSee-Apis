package com.what2see.service.user;

import com.what2see.dto.user.GuideLoginDTO;
import com.what2see.dto.user.GuideRegisterDTO;
import com.what2see.mapper.user.GuideDTOMapper;
import com.what2see.model.user.Guide;
import com.what2see.repository.user.GuideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GuideService {

    private final GuideRepository guideRepository;

    private final GuideDTOMapper guideMapper;

    public Guide register(GuideRegisterDTO dto) throws DataIntegrityViolationException {
        Guide t = guideMapper.convertRegister(dto);
        return guideRepository.save(t);
    }

    public Guide login(GuideLoginDTO dto) {
        Guide t = guideRepository.authenticate(dto.getUsername(), dto.getPassword());
        return t;
    }

    public Optional<Guide> findById(Long guideId) {
        return guideRepository.findById(guideId);
    }
}
