package com.what2see.service.user;

import com.what2see.dto.user.UserLoginDTO;
import com.what2see.model.user.User;
import com.what2see.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService<T extends User> {

    private final UserRepository<T> userRepository;

    public List<T> getAll() {
        return this.userRepository.findAll();
    }

    public T register(T t) throws DataIntegrityViolationException {
        return userRepository.save(t);
    }

    public T login(UserLoginDTO dto) {
        return userRepository.findUserByUsernameAndPassword(dto.getUsername(), dto.getPassword()).orElseThrow();
    }

    public T findById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

}
