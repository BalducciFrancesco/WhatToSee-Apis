package com.what2see.service.user;

import com.what2see.dto.user.UserLoginDTO;
import com.what2see.model.user.User;
import com.what2see.repository.user.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

// TODO using factory method pattern?

public abstract class UserService<T extends User> {

    public abstract UserRepository<T> getRepository();

    public List<T> getAll() {
        return this.getRepository().findAll();
    }

    public T register(T t) throws DataIntegrityViolationException {
        return getRepository().save(t);
    }

    public T login(UserLoginDTO dto) {
        return getRepository().authenticate(dto.getUsername(), dto.getPassword()).orElse(null);
    }

    public T findById(Long userId) {
        return getRepository().findById(userId).orElseThrow();
    }

}
