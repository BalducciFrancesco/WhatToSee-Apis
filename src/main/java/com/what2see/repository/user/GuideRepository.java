package com.what2see.repository.user;

import com.what2see.model.user.Guide;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository that provides CRUD operations for {@link Guide} entities.<br>
 * Only overrides interface method for typing purposes.<br>
 * Should be injected as <code>UserRepository&lt;Guide&gt;</code> for type safety.
 * @see UserRepository
 */
@Repository
public interface GuideRepository extends UserRepository<Guide> {

    /**
     * Authenticates a {@link Guide} with the given username and password.
     * @param username username to test with
     * @param password password to test with
     * @return authenticated entity if found, {@link Optional#empty()} otherwise
     */
    @Override
    @Query("select t from Guide t where t.username = :username and t.password = :password")
    Optional<Guide> authenticate(String username, String password);

}
