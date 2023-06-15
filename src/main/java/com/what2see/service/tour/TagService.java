package com.what2see.service.tour;

import com.what2see.model.tour.Tag;
import com.what2see.repository.tour.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
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

    public List<Tag> createByNames(List<String> tagNames) { // if not exists already
        List<Tag> result = new ArrayList<>();
        for(String tagName : tagNames) {
            Tag t = tagRepository.findByName(tagName).orElse(null);
            if(t == null) {
                t = tagRepository.save(Tag.builder().name(tagName).build());
            }
            result.add(t);
        }
        return result;
    }
}
