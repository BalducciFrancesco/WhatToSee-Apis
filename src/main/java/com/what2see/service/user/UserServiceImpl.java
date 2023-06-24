package com.what2see.service.user;

import com.what2see.model.user.User;
import com.what2see.repository.user.UserRepository;
import com.what2see.repository.user.UserRepositoryImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the <b>Factory Method</b> design pattern.<br>
 * Service class that handles the business logic for abstract {@link User} entities, independently of their concrete type.<br>
 * Its only use it to provide the concrete instance of the {@link UserRepository} interface to the {@link UserService} superclass,
 * which contains the generic business logic for all {@link com.what2see.model.user.User} entities.<br>
 * Should be injected as <code>UserService&lt;User&gt;</code> for type safety.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl extends UserService<User> {

    // dependencies autowired by spring boot

    private final UserRepositoryImpl userRepository;

    /**
     * Returns the concrete instance of the {@link UserRepository} interface for {@link User} entities.
     * @return repository instance
     */
    @Override
    public UserRepository<User> getRepository() {
        return this.userRepository;
    }
}
