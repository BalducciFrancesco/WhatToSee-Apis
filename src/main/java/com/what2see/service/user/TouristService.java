package com.what2see.service.user;

import com.what2see.model.user.Tourist;
import com.what2see.repository.user.TouristRepository;
import com.what2see.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the <b>Factory Method</b> design pattern.<br>
 * Service class that handles the business logic for {@link Tourist} entities.<br>
 * Its only use it to provide the concrete instance of the {@link UserRepository} interface to the {@link UserService} superclass,
 * which contains the generic business logic for all {@link com.what2see.model.user.User} entities.<br>
 * Should be injected as <code>UserService&lt;Tourist&gt;</code> for type safety.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TouristService extends UserService<Tourist> {

    // dependencies autowired by spring boot

    private final TouristRepository touristRepository;

    /**
     * Returns the concrete instance of the {@link UserRepository} interface for {@link Tourist} entities.
     * @return repository instance
     */
    @Override
    public UserRepository<Tourist> getRepository() {
        return this.touristRepository;
    }
}
