package com.what2see.service.tour;

import com.what2see.dto.tour.TourCreateDTO;
import com.what2see.mapper.tour.TourDTOMapper;
import com.what2see.model.tour.Tour;
import com.what2see.repository.tour.TourRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TourService {

    private final TourRepository tourRepository;
    private final TourDTOMapper tourMapper;

    public Tour create(TourCreateDTO dto, Long guideId) {
        Tour t = tourMapper.convertCreate(dto, guideId);
        return tourRepository.save(t);
    }
}
