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

/**
 * Service that converts {@link Report} entities from and to DTOs.<br>
 * Is usually used in controller to communicate with client side.
 */
@RequiredArgsConstructor
@Service
public class ReportDTOMapper {

    // dependencies autowired by spring boot

    private final UserDTOMapper userMapper;

    private final UserService<Tourist> touristService;

    private final TourService tourService;

    /**
     * Converts a {@link Report} entity to a {@link ReportResponseDTO DTO} that can be sent to client
     * @param report entity to be converted
     * @return DTO that can be sent to client
     */
    public ReportResponseDTO convertResponse(Report report) {
        return ReportResponseDTO.builder()
                .id(report.getId())
                .author(userMapper.convertResponse(report.getAuthor()))
                .description(report.getDescription())
                .build();
    }

    /**
     * Converts a client-sent {@link ReportCreateDTO DTO} to a {@link Report} entity that can be persisted
     * @param r DTO to be converted
     * @param tourId id of the tour to be reported
     * @param touristAuthorId id of the tourist that is reporting
     * @return entity that can be persisted
     */
    public Report convertCreate(ReportCreateDTO r, Long tourId, Long touristAuthorId) {
        return Report.builder()
                .tour(tourService.findById(tourId))
                .author(touristService.findById(touristAuthorId))
                .description(r.getDescription())
                .build();
    }
}
