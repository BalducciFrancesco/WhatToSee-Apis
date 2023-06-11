package com.what2see.repository.user;

import com.what2see.model.user.Guide;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuideRepository extends UserRepository<Guide> {

    @Query("select t from Guide t where t.username = :username and t.password = :password")
    Optional<Guide> authenticate(String username, String password);

}
