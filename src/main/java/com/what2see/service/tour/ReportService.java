package com.what2see.service.tour;

import com.what2see.exception.InteractionAlreadyPerformedException;
import com.what2see.model.tour.Report;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Tourist;
import com.what2see.repository.tour.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class that handles the business logic for {@link Report} entities.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    // dependencies autowired by spring boot

    private final ReportRepository reportRepository;

    /**
     * Creates a new {@link Report} entity.
     * @param r report to be created (without id)
     * @return created report (with id)
     * @throws InteractionAlreadyPerformedException if the author (tourist) of the report has already reported the tour
     */
    public Report create(Report r) throws InteractionAlreadyPerformedException {
        Tour creatingTour = r.getTour();
        Tourist creatingTourist = r.getAuthor();

        // check if the tourist has already reported the tour
        if(creatingTourist.getReportedTours().stream().anyMatch(rr -> rr.getTour().equals(creatingTour)))
            throw new InteractionAlreadyPerformedException(creatingTour, creatingTourist);

        return reportRepository.save(r);
    }
}
