package com.what2see.service.tour;

import com.what2see.mapper.tour.TagDTOMapper;
import com.what2see.model.tour.Tag;
import com.what2see.repository.tour.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final TagDTOMapper tagMapper;

    public List<Tag> getAll() {
        return this.tagRepository.findAll();
    }

    public List<Tag> findOrCreateTags(List<String> tagNames) {
        List<Tag> tags = new ArrayList();
        for(String tagName : tagNames) {
            Tag t = tagRepository.findByName(tagName);
            if(t == null) {
                t = tagRepository.save(Tag.builder().name(tagName).build());
            }
            tags.add(t);
        }
        return tags;
    }
}
