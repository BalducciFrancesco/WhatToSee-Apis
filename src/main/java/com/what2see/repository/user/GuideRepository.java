package com.what2see.repository.user;

import com.what2see.model.user.Guide;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface GuideRepository extends JpaRepository<Guide, Long> {

    @Query("select g from Guide g where g.username = :user and g.password = :pass")
    Guide authenticate(@Param("user")String username, @Param("pass")String password);
}
