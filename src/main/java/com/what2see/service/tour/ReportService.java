package com.what2see.service.tour;

import com.what2see.model.tour.Report;
import com.what2see.repository.tour.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public Report create(Report r) {
        return reportRepository.save(r);
    }
}