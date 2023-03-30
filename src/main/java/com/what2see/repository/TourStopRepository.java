package com.what2see.repository;

import com.what2see.model.tour.TourStop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourStopRepository extends JpaRepository<TourStop, Long> {
}
