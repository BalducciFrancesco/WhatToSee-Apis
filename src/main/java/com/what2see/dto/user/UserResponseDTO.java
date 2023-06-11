package com.what2see.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private @NotBlank Long id;

    private @NotBlank String username;

    private @NotBlank String firstName;

    private @NotBlank String lastName;

    private @NotBlank int role;
}