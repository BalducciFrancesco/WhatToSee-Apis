package com.what2see.service.tour;

import com.what2see.mapper.tour.TourDTOMapper;
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

}
