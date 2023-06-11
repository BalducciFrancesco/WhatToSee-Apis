package com.what2see.repository.user;

import com.what2see.model.user.Tourist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TouristRepository extends UserRepository<Tourist> {

    @Query("select t from Tourist t where t.username = :username and t.password = :password")
    Optional<Tourist> authenticate(String username, String password);

}
