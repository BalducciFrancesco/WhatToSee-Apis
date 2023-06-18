package com.what2see.repository.tour;

import com.what2see.model.tour.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository that provides CRUD operations for {@link Report} entities.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
