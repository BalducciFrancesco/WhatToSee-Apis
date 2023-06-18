package com.what2see.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Implementation of <b>DTO</b> pattern for <i>searching</i> tours in the <b>incoming</b> body.<br>
 * Note that all fields are optional.
 * @see com.what2see.model.tour.Tour
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourSearchDTO {

    private Long cityId;

    private List<Long> tagIds;

    private Long themeId;

    private String approxDuration;  // will search for <= this value

}
