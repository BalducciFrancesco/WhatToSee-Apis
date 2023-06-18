package com.what2see.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Implementation of <b>DTO</b> pattern for <i>returning</i> conversations in the <b>outgoing</b> body.<br>
 * Note that the messages field can be null ({@link com.what2see.controller.user.ConversationController#getAll see example}) for performance reasons.
 * @see com.what2see.model.user.Conversation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponseDTO {

    private @NotNull Long id;

    private @NotNull UserResponseDTO tourist;

    private @NotNull UserResponseDTO guide;

    private List<MessageResponseDTO> messages;

}