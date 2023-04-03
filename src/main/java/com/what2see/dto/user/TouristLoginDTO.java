package com.what2see.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TouristLoginDTO {

    private @NotBlank String username;

    private @NotBlank String password;

}
