package com.what2see.mapper.tour;

import com.what2see.dto.tour.ReportCreateDTO;
import com.what2see.dto.tour.ReportResponseDTO;
import com.what2see.mapper.user.TouristDTOMapper;
import com.what2see.model.tour.Report;
import com.what2see.service.tour.TourService;
import com.what2see.service.user.TouristService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ReportDTOMapper {

    private final TouristDTOMapper touristMapper;

    private final TouristService touristService;

    private final TourService tourService;

    public ReportResponseDTO convertResponse(Report report) {
        return ReportResponseDTO.builder()
                .id(report.getId())
                .author(touristMapper.convertResponse(report.getAuthor()))
                .description(report.getDescription())
                .build();
    }

    public Report convertCreate(ReportCreateDTO r, Long tourId, Long touristAuthorId) throws NoSuchElementException {
        return Report.builder()
                .tour(tourService.findById(tourId).orElseThrow())
                .author(touristService.findById(touristAuthorId).orElseThrow())
                .description(r.getDescription())
                .build();
    }
}
