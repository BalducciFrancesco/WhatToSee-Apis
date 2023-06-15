package com.what2see.service.tour;

import com.what2see.EntityMock;
import com.what2see.model.tour.Tag;
import com.what2see.repository.tour.TagRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TagServiceTest {

    private final EntityMock mock;

    private final TagService tagService;

    @SpyBean
    private final TagRepository tagRepository;


    @Test
    void findAll() {
        // setup
        List<Tag> expected = mock.getAllTags();
        // under test
        List<Tag> underTest = tagService.findAll();
        // assertions
        assertEquals(expected.size(), underTest.size());
        assertTrue(underTest.stream().anyMatch(c -> c.getName().equals("Divertente")));
        assertTrue(underTest.stream().anyMatch(c -> c.getName().equals("Gratuito")));
        assertTrue(underTest.stream().anyMatch(c -> c.getName().equals("Accessibile")));
        assertTrue(underTest.stream().anyMatch(c -> c.getName().equals("Breve")));
    }

    @Test
    void findAllByNames() {
        // setup
        List<String> names = List.of("Divertente", "Breve");
        List<Tag> expected = mock.getAllTags().stream().filter(t -> names.contains(t.getName())).toList();
        // under test
        List<Tag> underTest = tagService.findAllByNames(names);
        // assertions
        assertEquals(expected.size(), underTest.size());
        assertTrue(underTest.stream().anyMatch(c -> c.getName().equals("Divertente")));
        assertTrue(underTest.stream().anyMatch(c -> c.getName().equals("Breve")));
    }

    @Test
    void findAllById() {
        // setup
        List<Long> expected = mock.getAllTags().stream().map(Tag::getId).toList();
        // under test
        List<Tag> underTest = tagService.findAllById(expected);
        // assertions
        assertEquals(expected.size(), underTest.size());
        assertTrue(underTest.stream().map(Tag::getId).allMatch(expected::contains));
    }

    @Test
    void create() {
        // setup
        List<String> names = List.of("Tag1", "Divertente", "Tag2");
        List<Tag> expected = names.stream().map(n -> Tag.builder().name(n).build()).toList();
        // under test
        List<Tag> underTest = tagService.createByNames(names);
        // assertions
        assertEquals(expected.size(), underTest.size());
        assertTrue(underTest.stream().map(Tag::getName).allMatch(names::contains));
        assertTrue(underTest.stream().map(Tag::getId).allMatch(Objects::nonNull));
        verify(tagRepository, times(2)).save(any());    // shouldn't have called save on already existing tag
    }
}