package com.what2see.service.user;

import com.what2see.EntityMock;
import com.what2see.dto.user.UserLoginDTO;
import com.what2see.model.user.Administrator;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.model.user.User;
import com.what2see.repository.user.AdministratorRepository;
import com.what2see.repository.user.GuideRepository;
import com.what2see.repository.user.TouristRepository;
import com.what2see.repository.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link UserService} and its implementations.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserServiceTest {

    // dependencies autowired by spring boot

    private final EntityMock mock;

    private final UserService<User> userService;

    private final UserService<Tourist> touristService;

    private final UserService<Guide> guideService;

    private final UserService<Administrator> administratorService;

    /**
     * Tests {@link UserService#getRepository()} for each available role.<br>
     * Ensures that the expected repository is returned.
     */
    @Test
    void getRepository() {
        assertTrue(userService.getRepository() instanceof UserRepositoryImpl && userService.getRepository() != null);
        assertTrue(touristService.getRepository() instanceof TouristRepository && touristService.getRepository() != null);
        assertTrue(guideService.getRepository() instanceof GuideRepository && guideService.getRepository() != null);
        assertTrue(administratorService.getRepository() instanceof AdministratorRepository && administratorService.getRepository() != null);
    }

    /**
     * Tests all {@link UserService#findAll()} (for each available role) in the successful case.<br>
     * Ensures that all expected users (by id) are returned.
     */
    @Test
    void getAll() {
        // setup
        List<Long> expectedUsersIds = mock.getAllUsers().stream().map(User::getId).toList();
        List<Long> expectedTouristsIds = mock.getAllTourists().stream().map(User::getId).toList();
        List<Long> expectedGuidesIds = mock.getAllGuides().stream().map(User::getId).toList();
        List<Long> expectedAdministratorsIds = mock.getAllAdministrators().stream().map(User::getId).toList();
        // under test
        List<User> underTestUsers = userService.findAll();
        List<User> underTestTourists = touristService.findAll().stream().map(u -> (User) u).toList();
        List<User> underTestGuides = guideService.findAll().stream().map(u -> (User) u).toList();
        List<User> underTestAdministrators = administratorService.findAll().stream().map(a -> (User) a).toList();
        // assertion
        assertEquals(expectedUsersIds.size(), underTestUsers.size());
        assertEquals(expectedTouristsIds.size(), underTestTourists.size());
        assertEquals(expectedGuidesIds.size(), underTestGuides.size());
        assertEquals(expectedAdministratorsIds.size(), underTestAdministrators.size());
        assertTrue(underTestUsers.stream().map(User::getId).allMatch(expectedUsersIds::contains));
        assertTrue(underTestTourists.stream().map(User::getId).allMatch(expectedTouristsIds::contains));
        assertTrue(underTestGuides.stream().map(User::getId).allMatch(expectedGuidesIds::contains));
        assertTrue(underTestAdministrators.stream().map(User::getId).allMatch(expectedAdministratorsIds::contains));
    }

    /**
     * Tests all {@link UserService#register(User)} (for each available role) in the successful case.<br>
     * Ensures that all expected users (with all normalizations and hashing) are returned.
     */
    @Test
    void register() {
        // setup
        String touristString = " TOURIST 1 ";   // using this because register method has side effects
        Tourist expectedTourist = Tourist.builder()
                .username(touristString)
                .password(touristString)
                .firstName(touristString)
                .lastName(touristString)
                .build();
        String guideString = " GUIDE 1 ";   // using this because register method has side effects
        Guide expectedGuide = Guide.builder()
                .username(guideString)
                .password(guideString)
                .firstName(guideString)
                .lastName(guideString)
                .build();
        // under test
        User underTestTourist = touristService.register(expectedTourist);
        User underTestGuide = guideService.register(expectedGuide);
        // assertion
        assertNotNull(underTestTourist.getId());
        assertNotNull(underTestGuide.getId());
        assertEquals(touristString.toLowerCase().trim(), underTestTourist.getUsername());
        assertEquals(guideString.toLowerCase().trim(), underTestGuide.getUsername());
        assertNotEquals(touristString, underTestTourist.getPassword()); // hashed
        assertNotEquals(guideString, underTestGuide.getPassword()); // hashed
        assertEquals(touristString.trim(), underTestTourist.getFirstName());
        assertEquals(guideString.trim(), underTestGuide.getFirstName());
        assertEquals(touristString.trim(), underTestTourist.getLastName());
        assertEquals(guideString.trim(), underTestGuide.getLastName());
    }

    /**
     * Tests all {@link UserService#login(UserLoginDTO)} (for each available role) in the successful case.<br>
     * Ensures that all expected users (with all normalizations and hashing) are returned.
     */
    @Test
    void login() {
        // setup
        User expectedTourist = mock.getTourist();
        User expectedGuide = mock.getGuide();
        User expectedAdministrator = mock.getAdministrator();
        UserLoginDTO expectedTouristLogin = UserLoginDTO.builder()
                .username(" " + expectedTourist.getUsername().toUpperCase() + " ")  // should ignore leading and trailing spaces and casing
                .password("123")    // if did expectedTourist.getPassword() would get hash
                .build();
        UserLoginDTO expectedGuideLogin = UserLoginDTO.builder()
                .username(" " + expectedGuide.getUsername().toUpperCase() + " ")  // should ignore leading and trailing spaces and casing
                .password("123")    // if did expectedTourist.getPassword() would get hash
                .build();
        UserLoginDTO expectedAdministratorLogin = UserLoginDTO.builder()
                .username(" " + expectedAdministrator.getUsername().toUpperCase() + " ")  // should ignore leading and trailing spaces and casing
                .password("123")    // if did expectedTourist.getPassword() would get hash
                .build();
        // under test
        User underTestTourist = touristService.login(expectedTouristLogin);
        User underTestTouristNotFound = guideService.login(expectedTouristLogin);
        User underTestGuide = guideService.login(expectedGuideLogin);
        User underTestAdministrator = administratorService.login(expectedAdministratorLogin);
        // assertion
        assertNotNull(underTestTourist);
        assertNull(underTestTouristNotFound);
        assertNotNull(underTestGuide);
        assertNotNull(underTestAdministrator);
        assertEquals(expectedTourist.getId(), underTestTourist.getId());
        assertEquals(expectedGuide.getId(), underTestGuide.getId());
        assertEquals(expectedAdministrator.getId(), underTestAdministrator.getId());
    }

    /**
     * Tests all {@link UserService#findById(Long)} (for each available role) in the successful and failing case.<br>
     * Ensures that all expected users (by id and username) are returned and that a {@link NoSuchElementException} is thrown when the user is not found.
     */
    @Test
    void findById() {
        // setup
        User expectedTourist = mock.getTourist();
        User expectedGuide = mock.getGuide();
        User expectedAdministrator = mock.getAdministrator();
        // under test
        User underTestTourist = touristService.findById(expectedTourist.getId());
        User underTestGuide = guideService.findById(expectedGuide.getId());
        User underTestAdministrator = administratorService.findById(expectedAdministrator.getId());
        // assertions
        assertThrows(NoSuchElementException.class, () -> guideService.findById(expectedAdministrator.getId()));
        assertEquals(expectedTourist.getId(), underTestTourist.getId());
        assertEquals(expectedTourist.getUsername(), underTestTourist.getUsername());
        assertEquals(expectedGuide.getId(), underTestGuide.getId());
        assertEquals(expectedGuide.getUsername(), underTestGuide.getUsername());
        assertEquals(expectedAdministrator.getId(), underTestAdministrator.getId());
        assertEquals(expectedAdministrator.getUsername(), underTestAdministrator.getUsername());

    }
}