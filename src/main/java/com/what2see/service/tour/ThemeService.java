package com.what2see.service.tour;

import com.what2see.model.tour.Theme;
import com.what2see.repository.tour.ThemeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;

    public List<Theme> getAll() {
        return this.themeRepository.findAll();
    }

    public Optional<Theme> findById(Long themeId) {
        return themeRepository.findById(themeId);
    }
}
