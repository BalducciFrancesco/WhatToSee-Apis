package com.what2see.service.user;

import com.what2see.model.user.Tourist;
import com.what2see.repository.user.TouristRepository;
import com.what2see.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TouristService extends UserService<Tourist> {

    private final TouristRepository touristRepository;

    @Override
    public UserRepository<Tourist> getRepository() {
        return this.touristRepository;
    }
}
