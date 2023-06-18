package com.what2see.repository.user;

import com.what2see.model.user.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository that provides CRUD operations for {@link Conversation} entities.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * Find conversation by their participants.
     * @param touristId search criteria
     * @param guideId search criteria
     * @return conversation if found, {@link Optional#empty()} otherwise
     */
    Optional<Conversation> findByTouristIdAndGuideId(Long touristId, Long guideId);

}
