package com.what2see.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {

    private @NotBlank String username;

    private @NotBlank String password;

}
