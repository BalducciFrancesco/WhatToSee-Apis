package com.what2see.repository.tour;

import com.what2see.model.tour.Tour;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TourRepository extends JpaRepository<Tour, Long> {
}
