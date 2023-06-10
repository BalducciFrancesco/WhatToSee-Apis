package com.what2see.repository.user;

import com.what2see.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository<T extends User> extends JpaRepository<T, Long> {

    T findUserByUsernameAndPassword(String username, String password);

}
