package com.what2see.service.user;

import com.what2see.model.user.User;
import com.what2see.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl extends UserService<User> {

    private final UserRepository<User> userRepository;

    @Override
    public UserRepository<User> getRepository() {
        return this.userRepository;
    }
}
