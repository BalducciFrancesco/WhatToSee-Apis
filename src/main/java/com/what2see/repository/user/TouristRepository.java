package com.what2see.repository.user;

import com.what2see.model.user.Tourist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository that provides CRUD operations for {@link Tourist} entities.<br>
 * Only overrides interface method for typing purposes.<br>
 * Should be injected as <code>UserRepository&lt;Tourist&gt;</code> for type safety.
 * @see UserRepository
 */
@Repository
public interface TouristRepository extends UserRepository<Tourist> {

    /**
     * Authenticates a {@link Tourist} with the given username and password.
     * @param username username to test with
     * @param password password to test with
     * @return authenticated entity if found, {@link Optional#empty()} otherwise
     */
    @Override
    @Query("select t from Tourist t where t.username = :username and t.password = :password")
    Optional<Tourist> authenticate(String username, String password);

}
