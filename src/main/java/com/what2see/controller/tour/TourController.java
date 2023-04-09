package com.what2see.controller.tour;


import com.what2see.mapper.tour.TourDTOMapper;
import com.what2see.service.tour.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tour")
public class TourController {

    private final TourService tourService;

    private final TourDTOMapper tourMapper;

}
