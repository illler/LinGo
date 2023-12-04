package com.example.backend.services.props;

import com.example.backend.DTO.MessageDTO;
import com.example.backend.model.Message;
import com.example.backend.repositories.MessageRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Message Service Tests")
class MessageServiceTest {

    @Mock
    MessageRepository messageRepository;

    @InjectMocks
    MessageService messageService;
    Message message;


    @BeforeEach
    void init() {
        message = createTestMessage("sender", "recipient", "test message");
    }

    private Message createTestMessage(String senderId, String recipientId, String messageText) {
        return Message.builder()
                .senderId(senderId)
                .recipientId(recipientId)
                .originalMessage(messageText)
                .createAt(new Date())
                .build();
    }
    @Test
    @Order(1)
    @DisplayName("Save Message: When Message is Provided, Then Return Message")
    void testSaveMessage_whenMessageIsProvided_thenReturnMessage(){
        when(messageRepository.save(any(Message.class))).thenReturn(new Message());

        Message savedMessage = messageService.saveMessage(message);

        assertEquals(message.getSenderId(), savedMessage.getSenderId(),
                "Sender IDs should match");
        assertEquals(message.getRecipientId(), savedMessage.getRecipientId(),
                "Recipient IDs should match");
        assertEquals(message.getCreateAt(), savedMessage.getCreateAt(),
                "Creation dates should match");
        assertEquals(message.getOriginalMessage(), savedMessage.getOriginalMessage(),
                "Message content should match");
    }

    @Test
    @Order(2)
    @DisplayName("Save Multiple Messages: When Messages Are Provided, Then All Messages Should Be Saved")
    void testSaveMultipleMessages_whenMessagesAreProvided_thenAllMessagesShouldBeSaved() {
        // Arrange
        Message message1 = createTestMessage("sender", "recipient", "test message 1");
        Message message2 = createTestMessage("sender", "recipient", "test message 2");
        Message message3 = createTestMessage("sender", "recipient", "test message 3");
        Message message4 = createTestMessage("sender", "recipient", "test message 4");

        when(messageRepository.save(any(Message.class)))
                .thenReturn(new Message());

        // Act
        Message savedMessage1 = messageService.saveMessage(message1);
        Message savedMessage2 = messageService.saveMessage(message2);
        Message savedMessage3 = messageService.saveMessage(message3);
        Message savedMessage4 = messageService.saveMessage(message4);

        // Assert
        assertMessageEquality("Message 1", message1, savedMessage1);
        assertMessageEquality("Message 2", message2, savedMessage2);
        assertMessageEquality("Message 3", message3, savedMessage3);
        assertMessageEquality("Message 4", message4, savedMessage4);
    }

    private void assertMessageEquality(String messageDescription, Message expected, Message actual) {
        assertEquals(expected.getSenderId(), actual.getSenderId(),
                messageDescription + ": Sender ID should match");
        assertEquals(expected.getRecipientId(), actual.getRecipientId(),
                messageDescription + ": Recipient ID should match");
        assertEquals(expected.getCreateAt(), actual.getCreateAt(),
                messageDescription + ": Creation date should match");
        assertEquals(expected.getOriginalMessage(), actual.getOriginalMessage(),
                messageDescription + ": Message content should match");
    }



    @Test
    @Order(3)
    @DisplayName("Receive All Messages: When SenderId and RecipientId Are Not the Same, Then Return List MessageDTO")
    void testReceiveAllMessages_whenSenderIdAndRecipientIdIsNotTheSame_thenReturnListMessageDTO() {
        String senderId = "sender";
        String recipientId = "recipient";

        List<Message> testMessages = List.of(
                createTestMessage(senderId, recipientId, "Test message 1"),
                createTestMessage(senderId, recipientId, "Test message 2"),
                createTestMessage(senderId, recipientId, "Test message 3")
        );

        when(messageRepository.findAllBySenderIdAndRecipientIdOrderByCreateAt(senderId, recipientId))
                .thenReturn(testMessages);

        List<Message> messageList = messageService.receiveAllMessage(senderId, recipientId);

        assertNotNull(messageList, "The returned messageList should not be null");
        assertEquals(testMessages.size(), messageList.size(), "The size of the returned messageList should match the size of testMessages");

    }

    @Test
    @Order(3)
    @DisplayName("Receive All Messages: When SenderId and RecipientId Are the Same, Then Return List MessageDTO")
    void testReceiveAllMessages_whenSenderIdAndRecipientIdIsTheSame_thenReturnListMessageDTO() {
        String senderId = "sender";
        String recipientId = "sender";

        List<Message> testMessages = List.of(
                createTestMessage(senderId, recipientId, "Test message 1"),
                createTestMessage(senderId, recipientId, "Test message 2"),
                createTestMessage(senderId, recipientId, "Test message 3")
        );

        when(messageRepository.findAllBySenderIdAndRecipientIdOrderByCreateAt(senderId, recipientId))
                .thenReturn(testMessages);

        List<Message> messageList = messageService.receiveAllMessage(senderId, recipientId);


        assertNotNull(messageList, "The returned messageList should not be null");
        assertFalse(messageList.isEmpty(), "The returned messageList should not be empty");
        assertEquals(testMessages.size(), messageList.size(), "The size of the returned messageList should match the size of testMessages");


    }
    @Test
    @DisplayName("Retrieve All Interlocutors: When Valid User ID Provided, Then Return Set of Interlocutors")
    void testReceivingAllInterlocutorsId() {
        String currentUserId = "user1";

        Set<String> interlocutorsFromCorrespondence = new HashSet<>(Set.of("user2", "user3"));
        Set<String> interlocutorsFromWriteUs = new HashSet<>(Set.of("user4", "user5"));

        when(messageRepository.findAllUsersIdWhoDoWeHaveCorrespondenceWith(currentUserId))
                .thenReturn(interlocutorsFromCorrespondence);

        when(messageRepository.findAllUsersIdWhoDoWriteUs(currentUserId))
                .thenReturn(interlocutorsFromWriteUs);

        Set<String> result = messageService.receivingAllInterlocutorsId(currentUserId);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.containsAll(interlocutorsFromCorrespondence),
                "Result should contain interlocutors from correspondence");
        assertTrue(result.containsAll(interlocutorsFromWriteUs),
                "Result should contain interlocutors from write us");
    }



}