package com.what2see.repository.tour;

import com.what2see.model.tour.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository that provides CRUD operations for {@link Tag} entities.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Find tag by name.
     * @param name search criteria
     * @return tag if found, {@link Optional#empty()} otherwise
     */
    Optional<Tag> findByName(String name);

    /**
     * Find all tags with the given names.
     * @param names search criteria
     * @return list of tags
     */
    List<Tag> findByNameIn(List<String> names);
}
