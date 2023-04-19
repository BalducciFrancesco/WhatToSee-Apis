package com.what2see.repository.tour;

import com.what2see.model.tour.City;
import com.what2see.model.tour.Theme;
import com.what2see.model.tour.Tour;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TourRepository extends JpaRepository<Tour, Long> {

    // all fields are optional filters for search
    // also not searching for tags, will be done code-side
    @Query("SELECT t FROM Tour t JOIN t.tags tt WHERE (:city IS NULL OR t.city = :city) AND (:theme IS NULL OR t.theme = :theme) AND (:approxDuration IS NULL OR t.approxDuration <= :approxDuration)")
    List<Tour> search(City city, Theme theme, String approxDuration);

}
