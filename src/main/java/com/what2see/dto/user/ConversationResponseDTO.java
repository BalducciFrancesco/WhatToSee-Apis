package com.what2see.dto.user;

import com.what2see.model.user.Message;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ConversationResponseDTO {

    private @NotNull Long id;

    private @NotNull TouristResponseDTO tourist;

    private @NotNull GuideResponseDTO guide;

    private List<Message> messages; // can be null in summary page


}