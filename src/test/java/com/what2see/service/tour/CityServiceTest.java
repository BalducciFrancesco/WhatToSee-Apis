package com.what2see.service.tour;

import com.what2see.EntityMock;
import com.what2see.model.tour.City;
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
public class CityServiceTest {

    private final EntityMock mock;

    private final CityService cityService;


    @Test
    void findAll() {
        // setup
        List<Long> expectedIds = mock.getAllCities().stream().map(City::getId).toList();
        // under test
        List<City> underTest = cityService.findAll();
        // assertions
        assertEquals(expectedIds.size(), underTest.size());
        assertTrue(underTest.stream().map(City::getId).allMatch(expectedIds::contains));
    }

    @Test
    void findById() {
        // setup
        City expected = mock.getCity();
        // under test
        City underTest = cityService.findById(expected.getId());
        // assertions
        assertEquals(expected.getId(), underTest.getId());
        assertEquals(expected.getName(), underTest.getName());
    }
}
