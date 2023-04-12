package com.what2see.repository.user;

import com.what2see.model.user.Administrator;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    @Query("select a from Administrator a where a.username = :user and a.password = :pass")
    Administrator authenticate(@Param("user")String username, @Param("pass")String password);
}
