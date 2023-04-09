package com.what2see.service.tour;

import com.what2see.mapper.tour.CityDTOMapper;
import com.what2see.model.tour.City;
import com.what2see.repository.tour.CityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CityService {

    private final CityRepository cityRepository;
    private final CityDTOMapper cityMapper;

    public List<City> getAll() {
        return this.cityRepository.findAll();
    }

}
