package com.what2see.service.tour;

import com.what2see.model.tour.Tag;
import com.what2see.repository.tour.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> findAll() {
        return this.tagRepository.findAll();
    }

    public List<Tag> findAllByNames(List<String> tagNames) {
        return tagRepository.findByNameIn(tagNames);
    }

    public List<Tag> findAllById(List<Long> tagIds) {
        return tagRepository.findAllById(tagIds);
    }

    public void create(List<String> tagNames) { // if not exists already
        for(String tagName : tagNames) {
            if(tagRepository.findByName(tagName) == null) {
                tagRepository.save(Tag.builder().name(tagName).build());
            }
        }
    }
}
