package com.what2see.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConversationCreateDTO {

    private @NotNull Long touristId;

    private @NotNull Long guideId;

    private @NotBlank String message;

}