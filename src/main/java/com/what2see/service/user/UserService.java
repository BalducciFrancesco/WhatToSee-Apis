package com.what2see.service.user;

import com.what2see.dto.user.UserLoginDTO;
import com.what2see.model.user.User;
import com.what2see.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
        return userRepository.findUserByUsernameAndPassword(dto.getUsername(), dto.getPassword());
    }

    public Optional<T> findById(Long userId) {
        return userRepository.findById(userId);
    }

}
