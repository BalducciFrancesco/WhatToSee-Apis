package com.what2see.repository.tour;

import com.what2see.model.tour.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByName(String name);

    Tag findByName(String name);
}
