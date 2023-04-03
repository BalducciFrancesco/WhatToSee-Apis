package com.what2see.service;

import com.what2see.dto.user.GuideLoginDTO;
import com.what2see.dto.user.GuideRegisterDTO;
import com.what2see.mapper.GuideDTOMapper;
import com.what2see.model.user.Guide;
import com.what2see.repository.GuideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
}
