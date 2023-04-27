package com.what2see.service.tour;

import com.what2see.model.tour.Theme;
import com.what2see.repository.tour.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    public List<Theme> getAll() {
        return this.themeRepository.findAll();
    }

    public Theme findById(Long themeId) throws NoSuchElementException {
        return themeRepository.findById(themeId).orElseThrow();
    }
}
