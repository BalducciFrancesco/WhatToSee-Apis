package com.what2see.dto.tour;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

/**
 * Implementation of <b>DTO</b> pattern for <i>creating</i> reviews in the <b>incoming</b> body.<br>
 * Information about the author is implicitly obtained from other request parameters and creationDate is set to the
 * current timestamp
 * @see com.what2see.model.tour.Review
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDTO {

    private @NotNull @Range(min = 1, max = 5) Integer stars;

    private @NotBlank String description;

}
