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

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ThemeServiceTest {

    private final EntityMock mock;

    private final ThemeService themeService;


    @Test
    void findAll() {
        // setup
        List<Theme> expected = mock.getAllThemes();
        // under test
        List<Theme> underTest = themeService.findAll();
        // assertions
        assertEquals(expected.size(), underTest.size());
        assertTrue(underTest.stream().anyMatch(c -> c.getName().equals("Montagna")));
        assertTrue(underTest.stream().anyMatch(c -> c.getName().equals("Panoramico")));
        assertTrue(underTest.stream().anyMatch(c -> c.getName().equals("Musei")));
    }

    @Test
    void findById() {
        // setup
        Theme expected = mock.getTheme();
        // under test
        Theme underTest = themeService.findById(1L);
        // assertions
        assertEquals(expected.getId(), underTest.getId());
        assertEquals(expected.getName(), underTest.getName());
    }
}