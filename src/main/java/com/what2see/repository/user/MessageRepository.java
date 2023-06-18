package com.what2see.repository.user;

import com.what2see.model.user.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository that provides CRUD operations for {@link Message} entities.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
