package com.what2see.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MessageCreateDTO {

    private @NotNull String content;

    private @NotNull Boolean direction;

    private Long conversationId;    // can be null while creating contextually with conversation

}