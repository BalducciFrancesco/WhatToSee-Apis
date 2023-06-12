package com.what2see.repository.user;

import com.what2see.model.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryImpl extends UserRepository<User> {

    @Query("select t from User t where t.username = :username and t.password = :password")
    Optional<User> authenticate(String username, String password);

}
