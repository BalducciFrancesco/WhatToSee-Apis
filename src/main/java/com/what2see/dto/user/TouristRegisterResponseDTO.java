package com.what2see.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TouristRegisterResponseDTO {

    private @NotBlank Long id;

    private @NotBlank String username;

    private @NotBlank String firstName;

    private @NotBlank String lastName;

    private @Builder.Default @NotBlank int role = UserRole.TOURIST.ordinal();


}
