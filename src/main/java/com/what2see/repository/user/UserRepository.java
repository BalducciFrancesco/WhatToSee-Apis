package com.what2see.repository.user;

import com.what2see.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Repository that provides CRUD operations for {@link User} entities.<br>
 * This repository is used for typing purposes only and can't be injected directly.
 * @param <T> type of the {@link User} subclass (including {@link User} itself)
 */
@NoRepositoryBean
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

    /**
     * Authenticates a {@link User} subclass (including {@link User} itself) with the given username and password.<br>
     * This method must be overridden by all subclasses.
     * @param username username to test with
     * @param password password to test with
     * @return authenticated entity if found, {@link Optional#empty()} otherwise
     */
    Optional<T> authenticate(String username, String password);

}
