package com.what2see.service.tour;

import com.what2see.EntityMock;
import com.what2see.exception.InteractionAlreadyPerformedException;
import com.what2see.model.tour.Report;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Tourist;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ReportService}.
 */
@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ReportServiceTest {

    // dependencies autowired by spring boot

    private final EntityMock mock;

    private final ReportService reportService;

    /**
     * Tests {@link ReportService#create(Report)} in the successful case.<br>
     * Ensures that a new report (with a new id and the same description) is created.
     */
    @Test
    void create() {
        // setup
        Tourist subject = mock.getTourist();
        // find a tour that HAS NOT been reported by the subject (otherwise the test would fail)
        List<Long> subjectReportedToursIds = subject.getReportedTours().stream().map(Report::getTour).map(Tour::getId).toList();
        Tour expectedReportable = mock.getAllTours().stream().filter(t -> !subjectReportedToursIds.contains(t.getId())).findAny().orElseThrow();
        String expectedDescription = "Report1";
        Report expected = Report.builder()
                .author(subject)
                .description(expectedDescription)
                .tour(expectedReportable)
                .build();
        // under test
        Report underTest = reportService.create(expected);
        // assertion
        assertNotNull(underTest.getId());
        assertEquals(expected.getDescription(), underTest.getDescription());
    }

    /**
     * Tests {@link ReportService#create(Report)} in the unsuccessful case because of a duplicate report (same author, same tour).<br>
     * Ensures that a {@link InteractionAlreadyPerformedException} is thrown.
     */
    @Test
    void noMultipleReports() {
        // setup
        Tourist subject = mock.getTourist();
        // find a tour that HAS already been reported by the subject (otherwise the test would fail)
        Tour expectedNotReportable = subject.getReportedTours().stream().map(Report::getTour).findAny().orElseThrow();
        String expectedDescription = "Report2";
        Report expected = Report.builder()
                .author(subject)
                .description(expectedDescription)
                .tour(expectedNotReportable)
                .build();
        // under test and assertion
        assertThrows(InteractionAlreadyPerformedException.class, () -> reportService.create(expected));
    }

}