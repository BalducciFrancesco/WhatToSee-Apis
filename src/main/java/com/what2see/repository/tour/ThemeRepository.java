package com.what2see.repository.tour;

import com.what2see.model.tour.Theme;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
