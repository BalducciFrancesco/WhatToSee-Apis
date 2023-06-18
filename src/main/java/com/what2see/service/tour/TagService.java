package com.what2see.service.tour;

import com.what2see.model.tour.Tag;
import com.what2see.repository.tour.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that handles the business logic for {@link Tag} entities.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    // dependencies autowired by spring boot

    private final TagRepository tagRepository;

    /**
     * Returns all existing {@link Tag} entities.
     * @return list of all tags
     */
    public List<Tag> findAll() {
        return this.tagRepository.findAll();
    }

    /**
     * Returns all {@link Tag} entities with the given names.
     * @param tagNames names of the tags
     * @return list of tags with the given names
     */
    public List<Tag> findAllByNames(List<String> tagNames) {
        return tagRepository.findByNameIn(tagNames);
    }

    /**
     * Returns all {@link Tag} entities with the given ids.
     * @param tagIds ids of the tags
     * @return list of tags with the given ids
     */
    public List<Tag> findAllById(List<Long> tagIds) {
        return tagRepository.findAllById(tagIds);
    }

    /**
     * Creates multiple new {@link Tag} entities by their name.<br>
     * If a tag with the given name already exists, it will be reused.
     * @param tagNames names of the tags to be created
     * @return created tags (with id)
     */
    public List<Tag> createByNames(List<String> tagNames) { // if not exists already
        List<Tag> result = new ArrayList<>();
        for(String tagName : tagNames) {
            // check if tag already exists
            Tag t = tagRepository.findByName(tagName).orElse(null);
            if(t == null) { // not exists
                t = tagRepository.save(Tag.builder().name(tagName).build());
            }
            result.add(t);
        }
        return result;
    }
}
