package com.what2see.service.user;

import com.what2see.EntityMock;
import com.what2see.model.user.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link MessageService}.
 */
@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MessageServiceTest {

    // dependencies autowired by spring boot

    private final EntityMock mock;

    private final MessageService messageService;

    /**
     * Tests {@link MessageService#sendMessage(Message)} in the successful case.<br>
     * Ensures that the expected message (by content, direction and corresponding conversation) is returned.
     */
    @Test
    void sendMessage() {
        // setup
        String expectedContent = "Message1";
        Message expected = Message.builder()
                .direction(false)
                .conversation(mock.getConversation())
                .content(expectedContent)
                .build();
        // under test
        Message underTest = messageService.sendMessage(expected);
        // assertion
        assertNotNull(underTest.getId());
        assertEquals(expected.getContent(), underTest.getContent());
        assertEquals(expected.getDirection(), underTest.getDirection());
        assertTrue(underTest.getConversation().getMessages().stream().map(Message::getContent).anyMatch(expectedContent::equals));
    }

}