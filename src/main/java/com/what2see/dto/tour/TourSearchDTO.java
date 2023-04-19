package com.what2see.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TourSearchDTO {

    // all fields are optional

    private Long cityId;

    private List<Long> tagIds;

    private Long themeId;

    private String approxDuration;  // will search for <= this value

}
