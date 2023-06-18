package com.what2see.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Implementation of <b>DTO</b> pattern for <i>returning</i> users in the <b>incoming</b> body.<br>
 * Note that the {@link UserResponseDTO#role} field is mapped based on the actual {@link com.what2see.model.user.User} subclass.
 * This is done in order to be more flexible from the client side.
 * @see com.what2see.model.user.User
 * @see com.what2see.dto.user.UserRole
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private @NotBlank Long id;

    private @NotBlank String username;

    private @NotBlank String firstName;

    private @NotBlank String lastName;

    private @NotBlank int role; // mapped based on the actual User subclass and one of the UserRole values
}
