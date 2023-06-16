package com.what2see.service.user;

import com.what2see.dto.user.UserLoginDTO;
import com.what2see.model.user.User;
import com.what2see.repository.user.UserRepository;
import com.what2see.utils.PasswordManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

// TODO using factory method pattern?

public abstract class UserService<T extends User> {

    public abstract UserRepository<T> getRepository();

    public List<T> findAll() {
        return this.getRepository().findAll();
    }

    // FIXME side-effect
    public T register(T user) throws DataIntegrityViolationException {
        user.setUsername(normalize(user.getUsername()));
        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setPassword(PasswordManager.hashPassword(user.getPassword()));
        return getRepository().save(user);
    }

    // FIXME side-effect
    public T login(UserLoginDTO login) {
        login.setUsername(normalize(login.getUsername()));
        login.setPassword(PasswordManager.hashPassword(login.getPassword()));
        return getRepository().authenticate(login.getUsername(), login.getPassword()).orElse(null);
    }

    public T findById(Long userId) {
        return getRepository().findById(userId).orElseThrow();
    }

    private String normalize(String s) {
        return s.trim().toLowerCase();
    }

}
