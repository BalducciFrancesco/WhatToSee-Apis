package com.what2see.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterDTO {

    private @NotBlank String username;

    private @NotBlank String password;

    private @NotBlank String firstName;

    private @NotBlank String lastName;

}
