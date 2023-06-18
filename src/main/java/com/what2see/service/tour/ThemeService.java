package com.what2see.service.tour;

import com.what2see.model.tour.Theme;
import com.what2see.repository.tour.ThemeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class that handles the business logic for {@link Theme} entities.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ThemeService {

    // dependencies autowired by spring boot

    private final ThemeRepository themeRepository;

    /**
     * Returns all existing {@link Theme} entities.
     * @return list of all themes
     */
    public List<Theme> findAll() {
        return this.themeRepository.findAll();
    }

    /**
     * Returns a {@link Theme} entity with the given id.
     * @param themeId id of the theme
     * @return theme with the given id
     * @throws NoSuchElementException if no theme with the given id exists
     */
    public Theme findById(Long themeId) throws NoSuchElementException {
        return themeRepository.findById(themeId).orElseThrow();
    }
}
