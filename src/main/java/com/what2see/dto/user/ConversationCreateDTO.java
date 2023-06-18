package com.what2see.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementation of <b>DTO</b> pattern for <i>creating</i> conversations in the <b>incoming</b> body.<br>
 * Information about the author is implicitly obtained from other request parameters
 * @see com.what2see.model.user.Conversation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationCreateDTO {

    private @NotNull Long guideId;

    private @NotBlank String message;

}