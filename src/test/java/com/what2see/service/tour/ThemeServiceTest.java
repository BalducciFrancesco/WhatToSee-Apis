package com.what2see.service.tour;

import com.what2see.EntityMock;
import com.what2see.model.tour.Theme;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for {@link ThemeService}.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ThemeServiceTest {

    // dependencies autowired by spring boot

    private final EntityMock mock;

    private final ThemeService themeService;

    /**
     * Tests {@link ThemeService#findAll()}.<br>
     * Ensures that all expected themes (by id) are returned.
     */
    @Test
    void findAll() {
        // setup
        List<Long> expectedIds = mock.getAllThemes().stream().map(Theme::getId).toList();
        // under test
        List<Theme> underTest = themeService.findAll();
        // assertions
        assertEquals(expectedIds.size(), underTest.size());
        assertTrue(underTest.stream().map(Theme::getId).allMatch(expectedIds::contains));
    }

    /**
     * Tests {@link ThemeService#findById(Long)}.<br>
     * Ensures that the expected theme (by id and name) is returned.
     */
    @Test
    void findById() {
        // setup
        Theme expected = mock.getTheme();
        // under test
        Theme underTest = themeService.findById(expected.getId());
        // assertions
        assertEquals(expected.getId(), underTest.getId());
        assertEquals(expected.getName(), underTest.getName());
    }
}