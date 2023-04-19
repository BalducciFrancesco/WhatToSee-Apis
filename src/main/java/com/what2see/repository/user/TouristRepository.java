package com.what2see.repository.user;

import com.what2see.model.user.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long> {

    @Query("select t from Tourist t where t.username = :user and t.password = :pass")
    Tourist authenticate(@Param("user")String username, @Param("pass")String password);
}
