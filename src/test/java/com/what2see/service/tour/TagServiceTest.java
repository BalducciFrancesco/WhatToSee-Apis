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

/**
 * Test class for {@link TagService}.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TagServiceTest {

    // dependencies autowired by spring boot

    private final EntityMock mock;

    private final TagService tagService;

    /**
     * Mocked dependency just to verify that the service internally calls the repository an expected number of times.
     */
    @SpyBean
    private final TagRepository tagRepository;

    /**
     * Tests {@link TagService#findAll()}.<br>
     * Ensures that all expected tags (by id) are returned.
     */
    @Test
    void findAll() {
        // setup
        List<Long> expectedIds = mock.getAllTags().stream().map(Tag::getId).toList();
        // under test
        List<Tag> underTest = tagService.findAll();
        // assertions
        assertEquals(expectedIds.size(), underTest.size());
        assertTrue(underTest.stream().map(Tag::getId).allMatch(expectedIds::contains));
    }

    /**
     * Tests {@link TagService#findAllByNames(List)}.<br>
     * Ensures that all expected tags (by id and name) are returned.
     */
    @Test
    void findAllByNames() {
        // setup
        List<Tag> expected = mock.getAllTags().stream().limit(2).toList();
        List<Long> expectedIds = expected.stream().map(Tag::getId).toList();
        List<String> expectedNames = expected.stream().map(Tag::getName).toList();
        // under test
        List<Tag> underTest = tagService.findAllByNames(expectedNames);
        // assertions
        assertEquals(expectedIds.size(), underTest.size());
        assertTrue(underTest.stream().map(Tag::getId).allMatch(expectedIds::contains));
        assertTrue(underTest.stream().map(Tag::getName).allMatch(expectedNames::contains));
    }

    /**
     * Tests {@link TagService#findAllById(List)}.<br>
     * Ensures that all expected tags (by id) are returned.
     */
    @Test
    void findAllById() {
        // setup
        List<Long> expectedIds = mock.getAllTags().stream().map(Tag::getId).limit(2).toList();
        // under test
        List<Tag> underTest = tagService.findAllById(expectedIds);
        // assertions
        assertEquals(expectedIds.size(), underTest.size());
        assertTrue(underTest.stream().map(Tag::getId).allMatch(expectedIds::contains));
    }

    /**
     * Tests {@link TagService#createByNames(List)}.<br>
     * Ensures that all expected tags (by id and name) are returned and that the repository is called only for the tags that don't exist yet.
     */
    @Test
    void createByNames() {
        // setup
        List<String> names = List.of("Tag1", mock.getTag().getName(), "Tag2");  // note that the second tag is expected to exist
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