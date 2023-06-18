package com.what2see.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementation of <b>DTO</b> pattern for <i>creating</i> messages in the <b>incoming</b> body.<br>
 * Information about the author is implicitly obtained from other request parameters and mapped to the corresponding
 * boolean direction field as indicated by {@link com.what2see.model.user.Message#direction} logic.
 * @see com.what2see.model.user.Message
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateDTO {

    private @NotNull String content;

    private @NotNull Long conversationId;

}