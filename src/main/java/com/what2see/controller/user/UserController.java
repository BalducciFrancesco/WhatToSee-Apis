package com.what2see.controller.user;

import com.what2see.dto.user.UserLoginDTO;
import com.what2see.dto.user.UserRegisterDTO;
import com.what2see.dto.user.UserResponseDTO;
import com.what2see.dto.user.UserRole;
import com.what2see.mapper.user.GuideDTOMapper;
import com.what2see.mapper.user.TouristDTOMapper;
import com.what2see.mapper.user.UserDTOMapper;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.model.user.User;
import com.what2see.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for user endpoints
 * @see UserRole
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    // dependencies autowired by spring boot

    private final UserService<User> userService;

    private final UserService<Tourist> touristService;

    private final UserService<Guide> guideService;

    private final UserDTOMapper userMapper;

    private final TouristDTOMapper touristMapper;

    private final GuideDTOMapper guideMapper;


    /*
     * Some validations are not explicitly performed with try/catch's since RuntimeExceptions are expected to
     * be called and managed from the Spring Boot container in case of failed validation or user not found.
     */

    /**
     * Get all users of the specified role.<br>
     * It is intended to only be used for tourists or guides, since administrators are not supposed to be known.
     * @param roleId the role of the users to get
     * @return list of users of the specified role
     * @exception ResponseStatusException {@link HttpStatus#BAD_REQUEST} if the role is administrator
     */
    @GetMapping("/{roleId}")
    public ResponseEntity<List<UserResponseDTO>> getAll(@PathVariable int roleId) {
        UserRole role = UserRole.values()[roleId];
        Stream<User> response = switch(role) {
            case TOURIST -> touristService.findAll().stream().map(u -> (User) u);
            case GUIDE -> guideService.findAll().stream().map(u -> (User) u);
            case ADMINISTRATOR -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non è possibile conoscere gli amministratori");
        };
        return ResponseEntity.ok(response.map(userMapper::convertResponse).collect(Collectors.toList()));
    }

    /**
     * Attempts to log in a user with the specified credentials.<br>
     * Note that the {@link UserLoginDTO#username} field is trimmed and case-insensitive.
     * @param l the credentials of the user to log in
     * @return the logged user <i>or</i>
     * @exception ResponseStatusException {@link HttpStatus#BAD_REQUEST} if no user is found with the specified credentials
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid UserLoginDTO l) {
        User logged = userService.login(l); // used generic user service in order to not have to check the role (username is unique)
        if(logged != null) {
            return ResponseEntity.ok(userMapper.convertResponse(logged));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
    }

    /**
     * Attempts to register a new user with the specified credentials.<br>
     * It is intended to only be used for tourists or guides, since administrators are not supposed to be registered from outside the database.<br>
     * Note that the {@link UserRegisterDTO#username} field is trimmed and case-insensitive while
     * {@link UserRegisterDTO#firstName} and {@link UserRegisterDTO#lastName} fields are trimmed only.
     * @param roleId the role of the user to register
     * @param r the credentials and personal info of the user to register
     * @return the registered user
     * @exception ResponseStatusException {@link HttpStatus#BAD_REQUEST} if the username is already taken or if attempting to register an administrator
     */
    // username is trimmed and case-insensitive
    // first name and last name are trimmed
    @PostMapping("/{roleId}/register")
    public ResponseEntity<UserResponseDTO> register(@PathVariable int roleId, @RequestBody @Valid UserRegisterDTO r) {
        User created;
        UserRole role = UserRole.values()[roleId];
        try {
            created = switch(role) {
                case TOURIST -> touristService.register(touristMapper.convertRegister(r));
                case GUIDE -> guideService.register(guideMapper.convertRegister(r));
                case ADMINISTRATOR -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non è possibile registrare nuovi amministratori");
            };
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username già esistente");
        }
        return ResponseEntity.ok(userMapper.convertResponse(created));
    }

}
