package com.what2see.mapper.tour;

import com.what2see.dto.tour.ReportCreateDTO;
import com.what2see.dto.tour.ReportResponseDTO;
import com.what2see.mapper.user.TouristDTOMapper;
import com.what2see.model.tour.Report;
import com.what2see.service.tour.TourService;
import com.what2see.service.user.TouristService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ReportResponseDTO> convertResponse(List<Report> reviews) {
        return reviews.stream().map(this::convertResponse).collect(Collectors.toList());
    }

    public Report convertCreate(ReportCreateDTO r, Long tourId, Long touristAuthorId) {
        return Report.builder()
                .tour(tourService.findById(tourId))
                .author(touristService.findById(touristAuthorId))
                .description(r.getDescription())
                .build();
    }
}
