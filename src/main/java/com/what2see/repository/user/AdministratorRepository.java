package com.what2see.repository.user;

import com.what2see.model.user.Administrator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends UserRepository<Administrator> {

    @Query("select t from Guide t where t.username = :username and t.password = :password")
    Optional<Administrator> authenticate(String username, String password);

}
