package com.what2see.repository.user;

import com.what2see.model.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository that provides CRUD operations for {@link User} entities.<br>
 * Only overrides interface method for typing purposes.<br>
 * Should be injected as <code>UserRepository&lt;User&gt;</code> for type safety.
 * @see UserRepository
 */
@Repository
public interface UserRepositoryImpl extends UserRepository<User> {

    /**
     * Authenticates a {@link User}, independently of its specific subtype, with the given username and password.<br>
     * @param username username to test with
     * @param password password to test with
     * @return authenticated entity if found, {@link Optional#empty()} otherwise
     */
    @Override
    @Query("select t from User t where t.username = :username and t.password = :password")
    Optional<User> authenticate(String username, String password);

}
