package com.what2see.repository;

import com.what2see.model.user.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    @Query("select a from Administrator a where a.username = :user and a.password = :pass")
    Administrator authenticate(@Param("user")String username, @Param("pass")String password);
}
