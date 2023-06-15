package com.what2see.service.tour;

import com.what2see.model.tour.City;
import com.what2see.repository.tour.CityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<City> findAll() {
        return this.cityRepository.findAll();
    }

    public City findById(Long cityId) throws NoSuchElementException {
        return cityRepository.findById(cityId).orElseThrow();
    }
}
