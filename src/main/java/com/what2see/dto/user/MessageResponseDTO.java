package com.what2see.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class MessageResponseDTO {

    private @NotNull Long id;

    private @NotNull String content;

    private @NotNull Boolean direction;

    private @NotNull Date timestamp;

}