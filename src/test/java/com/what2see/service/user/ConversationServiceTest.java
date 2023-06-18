package com.what2see.service.user;

import com.what2see.EntityMock;
import com.what2see.exception.ConversationAlreadyStartedException;
import com.what2see.model.user.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConversationService}.
 */
@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ConversationServiceTest {

    // dependencies autowired by spring boot

    private final EntityMock mock;

    private final ConversationService conversationService;

    /**
     * Tests {@link ConversationService#findAll()}.<br>
     * Ensures that all expected conversations (by id) are returned.
     */
    @Test
    void getAll() {
        // setup
        List<Long> expectedIds = mock.getAllConversations().stream().map(Conversation::getId).toList();
        // under test
        List<Conversation> underTest = conversationService.findAll();
        // assertion
        assertEquals(expectedIds.size(), underTest.size());
        assertTrue(underTest.stream().map(Conversation::getId).allMatch(expectedIds::contains));
    }

    /**
     * Tests {@link ConversationService#findById(Long)} in the successful case.<br>
     * Ensures that the expected conversation (by id, id of participants and messages count) is returned.
     */
    @Test
    void findById() {
        // setup
        Conversation expected = mock.getConversation();
        // under test
        Conversation underTest = conversationService.findById(expected.getId());
        // assertions
        assertEquals(expected.getId(), underTest.getId());
        assertEquals(expected.getTourist().getId(), underTest.getTourist().getId());
        assertEquals(expected.getGuide().getId(), underTest.getGuide().getId());
        assertEquals(expected.getMessages().size(), underTest.getMessages().size());
    }

    /**
     * Tests {@link ConversationService#findByParticipants(Long, Long)} in the successful case.<br>
     * Ensures that the expected conversation (by id, id of participants and messages count) is returned.
     */
    @Test
    void findByParticipants() {
        // setup
        Conversation expected = mock.getConversation();
        // under test
        Conversation underTest = conversationService.findByParticipants(expected.getTourist().getId(), expected.getGuide().getId());
        // assertion
        assertEquals(expected.getId(), underTest.getId());
        assertEquals(expected.getTourist().getId(), underTest.getTourist().getId());
        assertEquals(expected.getGuide().getId(), underTest.getGuide().getId());
        assertEquals(expected.getMessages().size(), underTest.getMessages().size());
    }

    /**
     * Tests {@link ConversationService#isVisible(Conversation, Long)} in the successful and failing case.<br>
     * Ensure that the conversation is visible to the participants and not visible to others.
     */
    @Test
    void isVisible() {
        // setup
        Conversation expected = mock.getConversation();
        Long expectedTouristVisibleId = expected.getTourist().getId();
        Long expectedGuideVisibleId = expected.getGuide().getId();
        // find other another pair of participants that aren't participants (otherwise the test would fail)
        Long expectedTouristNotVisibleId = mock.getAllTourists().stream().map(User::getId).filter(u -> !expectedTouristVisibleId.equals(u)).findAny().orElseThrow();
        Long expectedGuideNotVisibleId = mock.getAllGuides().stream().map(User::getId).filter(u -> !expectedGuideVisibleId.equals(u)).findAny().orElseThrow();
        // under test
        boolean underTestTouristVisible = conversationService.isVisible(expected, expectedTouristVisibleId);
        boolean underTestGuideVisible = conversationService.isVisible(expected, expectedGuideVisibleId);
        boolean underTestTouristNotVisible = conversationService.isVisible(expected, expectedTouristNotVisibleId);
        boolean underTestGuideNotVisible = conversationService.isVisible(expected, expectedGuideNotVisibleId);
        // assertion
        assertTrue(underTestTouristVisible);
        assertTrue(underTestGuideVisible);
        assertFalse(underTestTouristNotVisible);
        assertFalse(underTestGuideNotVisible);
    }

    /**
     * Tests {@link ConversationService#startConversation(Conversation)} in the successful case with a single message from tourist.<br>
     * Ensures that the expected conversation (by id, id of participants and message count) is returned.
     */
    @Test
    void startConversation() {
        // setup
        Tourist subjectTourist = mock.getTourist();
        // find a guide that the tourist hasn't already started a conversation with (otherwise the test would fail)
        List<Long> subjectTouristConversationGuidesIds = subjectTourist.getConversations().stream().map(Conversation::getGuide).map(User::getId).toList();
        Guide subjectGuide = mock.getAllGuides().stream().filter(u -> !subjectTouristConversationGuidesIds.contains(u.getId())).findAny().orElseThrow();
        String expectedMessageContent = "Message1";
        Message expectedMessage = Message.builder()
                .content(expectedMessageContent)
                .direction(false)
                .build();
        Conversation expected = Conversation.builder()
                .guide(subjectGuide)
                .tourist(subjectTourist)
                .messages(List.of(expectedMessage))
                .build();
        expectedMessage.setConversation(expected);
        // under test
        Conversation underTest = conversationService.startConversation(expected);
        // assertion
        assertNotNull(underTest.getId());
        assertEquals(1, underTest.getMessages().size());
        assertEquals(subjectTourist.getId(), underTest.getTourist().getId());
        assertEquals(subjectGuide.getId(), underTest.getGuide().getId());
        assertEquals(expectedMessageContent, underTest.getMessages().get(0).getContent());
    }

    /**
     * Tests {@link ConversationService#startConversation(Conversation)} in the failing case because the conversation already exists.<br>
     * Ensures that a {@link ConversationAlreadyStartedException} is thrown.
     */
    @Test
    void startConversationNoMultipleBetweenParticipants() {
        // setup
        Tourist subjectTourist = mock.getTourist();
        // find a guide that the tourist has already started a conversation with (otherwise the test would fail)
        List<Long> subjectTouristConversationGuidesIds = subjectTourist.getConversations().stream().map(Conversation::getGuide).map(User::getId).toList();
        Guide subjectGuide = mock.getAllGuides().stream().filter(u -> subjectTouristConversationGuidesIds.contains(u.getId())).findAny().orElseThrow();
        String expectedMessageContent = "Message2";
        Message expectedMessage = Message.builder()
                .content(expectedMessageContent)
                .direction(false)
                .build();
        Conversation expected = Conversation.builder()
                .guide(subjectGuide)
                .tourist(subjectTourist)
                .messages(List.of(expectedMessage))
                .build();
        expectedMessage.setConversation(expected);
        // under test and assertion
        assertThrows(ConversationAlreadyStartedException.class, () -> conversationService.startConversation(expected));
    }

}