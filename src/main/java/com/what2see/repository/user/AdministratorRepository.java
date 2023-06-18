package com.what2see.repository.user;

import com.what2see.model.user.Administrator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository that provides CRUD operations for {@link Administrator} entities.<br>
 * Only overrides interface method for typing purposes.<br>
 * Should be injected as <code>UserRepository&lt;Administrator&gt;</code> for type safety.
 * @see UserRepository
 */
@Repository
public interface AdministratorRepository extends UserRepository<Administrator> {

    /**
     * Authenticates an {@link Administrator} with the given username and password.
     * @param username username to test with
     * @param password password to test with
     * @return authenticated entity if found, {@link Optional#empty()} otherwise
     */
    @Override
    @Query("select t from Administrator t where t.username = :username and t.password = :password")
    Optional<Administrator> authenticate(String username, String password);

}
