package com.what2see.repository.tour;

import com.what2see.model.tour.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository that provides CRUD operations for {@link Theme} entities.
 */
@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

}
