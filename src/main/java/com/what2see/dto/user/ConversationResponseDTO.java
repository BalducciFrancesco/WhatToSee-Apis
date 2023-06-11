package com.what2see.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponseDTO {

    private @NotNull Long id;

    private @NotNull UserResponseDTO tourist;

    private @NotNull UserResponseDTO guide;

    private @NotNull List<MessageResponseDTO> messages; // can be null in summary page


}