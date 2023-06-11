package com.what2see.service.user;

import com.what2see.model.user.Guide;
import com.what2see.repository.user.GuideRepository;
import com.what2see.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GuideService extends UserService<Guide> {

    private final GuideRepository guideRepository;
    
    @Override
    public UserRepository<Guide> getRepository() {
        return this.guideRepository;
    }
}
