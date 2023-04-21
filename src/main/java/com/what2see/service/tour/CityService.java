package com.what2see.service.tour;

import com.what2see.model.tour.City;
import com.what2see.repository.tour.CityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<City> getAll() {
        return this.cityRepository.findAll();
    }

    public Optional<City> findById(Long cityId) {
        return cityRepository.findById(cityId);
    }
}
