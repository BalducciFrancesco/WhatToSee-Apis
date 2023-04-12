package com.what2see.service.tour;

import com.what2see.mapper.tour.TagDTOMapper;
import com.what2see.model.tour.Tag;
import com.what2see.repository.tour.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        // TODO check if exists already, in order to not overwrite
        return tagNames.stream()
                .map(tagName -> Tag.builder().name(tagName).build())
                .map(tagRepository::save)
                .collect(Collectors.toList());
    }
}
