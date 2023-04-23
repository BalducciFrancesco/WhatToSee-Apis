package com.what2see.repository.tour;

import com.what2see.model.tour.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {
}
