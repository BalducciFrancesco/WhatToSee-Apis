package com.what2see.repository.user;

import com.what2see.model.user.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByTouristIdAndGuideId(Long touristId, Long guideId);

}
