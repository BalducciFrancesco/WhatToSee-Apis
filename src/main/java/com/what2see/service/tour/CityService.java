package com.what2see.service.tour;

import com.what2see.model.tour.City;
import com.what2see.repository.tour.CityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class that handles the business logic for {@link City} entities.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CityService {

    // dependencies autowired by spring boot

    private final CityRepository cityRepository;

    /**
     * Returns all existing {@link City} entities.
     * @return list of all cities
     */
    public List<City> findAll() {
        return this.cityRepository.findAll();
    }

    /**
     * Returns a {@link City} entity with the given id.
     * @param cityId id of the city
     * @return city with the given id
     * @throws NoSuchElementException if no city with the given id exists
     */
    public City findById(Long cityId) throws NoSuchElementException {
        return cityRepository.findById(cityId).orElseThrow();
    }
}
