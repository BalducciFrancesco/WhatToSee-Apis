package com.what2see.service.tour;

import com.what2see.exception.InteractionAlreadyPerformedException;
import com.what2see.model.tour.Report;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Tourist;
import com.what2see.repository.tour.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public Report create(Report r) throws InteractionAlreadyPerformedException {
        Tour creatingTour = r.getTour();
        Tourist creatingTourist = r.getAuthor();

        if(creatingTourist.getReportedTours().stream().anyMatch(rr -> rr.getTour().equals(creatingTour)))
            throw new InteractionAlreadyPerformedException(creatingTour, creatingTourist);

        return reportRepository.save(r);
    }
}
