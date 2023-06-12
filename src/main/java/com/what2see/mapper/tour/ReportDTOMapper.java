package com.what2see.mapper.tour;

import com.what2see.dto.tour.ReportCreateDTO;
import com.what2see.dto.tour.ReportResponseDTO;
import com.what2see.mapper.user.UserDTOMapper;
import com.what2see.model.tour.Report;
import com.what2see.model.user.Tourist;
import com.what2see.service.tour.TourService;
import com.what2see.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportDTOMapper {

    private final UserDTOMapper userMapper;

    private final UserService<Tourist> touristService;

    private final TourService tourService;

    public ReportResponseDTO convertResponse(Report report) {
        return ReportResponseDTO.builder()
                .id(report.getId())
                .author(userMapper.convertResponse(report.getAuthor()))
                .description(report.getDescription())
                .build();
    }

    public Report convertCreate(ReportCreateDTO r, Long tourId, Long touristAuthorId) {
        return Report.builder()
                .tour(tourService.findById(tourId))
                .author(touristService.findById(touristAuthorId))
                .description(r.getDescription())
                .build();
    }
}
