package com.what2see.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementation of <b>DTO</b> pattern for <i>registering</i> users in the <b>incoming</b> body.<br>
 * Note that the {@link UserRegisterDTO#username} field is trimmed and case-insensitive while
 * {@link UserRegisterDTO#firstName} and {@link UserRegisterDTO#lastName} fields are trimmed only.
 * @see com.what2see.model.user.User
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {

    private @NotBlank String username;

    private @NotBlank String password;

    private @NotBlank String firstName;

    private @NotBlank String lastName;

}
