package com.what2see.repository.tour;

import com.what2see.model.tour.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository that provides CRUD operations for {@link City} entities.
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

}
