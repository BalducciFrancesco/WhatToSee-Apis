package com.what2see.repository.tour;

import com.what2see.model.tour.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
