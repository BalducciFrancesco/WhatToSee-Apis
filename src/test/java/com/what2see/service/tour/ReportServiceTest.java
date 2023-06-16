package com.what2see.service.tour;

import com.what2see.EntityMock;
import com.what2see.model.tour.Report;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ReportServiceTest {

    private final EntityMock mock;

    private final ReportService reportService;


    @Test
    void create() {
        // setup
        Report expected = Report.builder()
                .author(mock.getTourist())
                .description("Report1")
                .tour(mock.getTour())
                .build();
        // under test
        Report underTest = reportService.create(expected);
        // assertion
        assertNotNull(underTest.getId());
        assertEquals(expected.getDescription(), underTest.getDescription());
    }

}