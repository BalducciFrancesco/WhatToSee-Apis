package com.what2see.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateDTO {

    // implicitly is from current user (direction = true if is guide, false if is tourist)

    private @NotNull String content;

    private @NotNull Long conversationId;

}