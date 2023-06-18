package com.what2see.service.user;

import com.what2see.dto.user.UserLoginDTO;
import com.what2see.model.user.User;
import com.what2see.repository.user.UserRepository;
import com.what2see.utils.PasswordManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

/**
 * Implementation of the <b>Factory Method</b> design pattern.<br>
 * Service class that handles the common business logic for all {@link User} subclasses, independently of their concrete type.
 * It is used for typing purposes only and can't be injected directly.<br>
 * Note that, in practice at the current state of code, the concrete repository is just a restriction of the pool of
 * {@link User} on which the methods are applied.<br>
 * @param <T> type of the {@link User} subclass (including {@link User} itself)
 */
public abstract class UserService<T extends User> {

    /**
     * Returns the concrete instance of the {@link UserRepository} interface for {@link User} entities.
     * This is the only method that needs to be implemented by the subclasses.
     * @return repository instance
     */
    public abstract UserRepository<T> getRepository();

    /**
     * Returns all {@link User} entities of type {@link T}.
     * @return list of all entities
     */
    public List<T> findAll() {
        return this.getRepository().findAll();
    }

    /**
     * Registers a new {@link User} entity of type {@link T}.<br>
     * The username is normalized to lowercase and trimmed.<br>
     * The first and last name are trimmed.<br>
     * The password is hashed.
     * @param user entity to be registered
     * @return registered entity
     * @throws DataIntegrityViolationException if the username is already taken
     * @see PasswordManager#hashPassword(String)
     */
    public T register(T user) throws DataIntegrityViolationException {
        user.setUsername(normalize(user.getUsername()));
        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setPassword(PasswordManager.hashPassword(user.getPassword()));
        return getRepository().save(user);
    }

    /**
     * Authenticates a {@link User} entity of type {@link T}.<br>
     * The username is normalized to lowercase and trimmed.<br>
     * The password is hashed.
     * @param login credentials to be authenticated
     * @return authenticated entity or null if the credentials are invalid
     */
    public T login(UserLoginDTO login) {
        login.setUsername(normalize(login.getUsername()));
        login.setPassword(PasswordManager.hashPassword(login.getPassword()));
        return getRepository().authenticate(login.getUsername(), login.getPassword()).orElse(null);
    }

    /**
     * Finds a {@link User} entity of type {@link T} by its id.
     * @param userId id of the entity to be found
     * @return found entity
     * @throws java.util.NoSuchElementException if the entity is not found
     */
    public T findById(Long userId) {
        return getRepository().findById(userId).orElseThrow();
    }

    /**
     * Normalizes a string to lowercase and trims it.
     * @param s string to be normalized
     * @return normalized string
     */
    private String normalize(String s) {
        return s.trim().toLowerCase();
    }

}
