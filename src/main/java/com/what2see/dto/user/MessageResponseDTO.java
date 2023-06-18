package com.what2see.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Implementation of <b>DTO</b> pattern for <i>returning</i> messages in the <b>outgoing</b> body
 * @see com.what2see.model.user.Message
 * @see com.what2see.model.user.Message#direction
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {

    private @NotNull Long id;

    private @NotNull String content;

    private @NotNull Boolean direction; // true if from guide to user

    private @NotNull Date timestamp;

}