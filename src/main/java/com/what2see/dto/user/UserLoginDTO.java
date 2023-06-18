package com.what2see.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementation of <b>DTO</b> pattern for <i>logging in</i> users in the <b>incoming</b> body.<br>
 * Note that the {@link UserLoginDTO#username} field is trimmed and case-insensitive.
 * @see com.what2see.model.user.User
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    private @NotBlank String username;

    private @NotBlank String password;

}
