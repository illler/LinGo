package com.example.backend.services.props;

import com.example.backend.DTO.MessageDTO;
import com.example.backend.model.Message;
import com.example.backend.repositories.MessageRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
                .message(messageText)
                .createAt(new Date())
                .build();
    }
    @Test
    @Order(1)
    void testSaveMessage_whenMessageIsProvided_thenReturnMessage(){
        when(messageRepository.save(any(Message.class))).thenReturn(new Message());

        Message savedMessage = messageService.saveMessage(message);

        assertEquals(message.getSenderId(), savedMessage.getSenderId());
        assertEquals(message.getRecipientId(), savedMessage.getRecipientId());
        assertEquals(message.getCreateAt(), savedMessage.getCreateAt());
        assertEquals(message.getMessage(), savedMessage.getMessage());
    }

    @Test
    @Order(2)
    void testSaveMultipleMessages_whenMessagesAreProvided_thenAllMessagesShouldBeSaved() {
        Message message1 = createTestMessage("sender", "recipient", "test message 1");
        Message message2 = createTestMessage("sender", "recipient", "test message 2");
        Message message3 = createTestMessage("sender", "recipient", "test message 3");
        Message message4 = createTestMessage("sender", "recipient", "test message 4");

        when(messageRepository.save(any(Message.class)))
                .thenReturn(new Message());

        Message savedMessage1 = messageService.saveMessage(message1);
        Message savedMessage2 = messageService.saveMessage(message2);
        Message savedMessage3 = messageService.saveMessage(message3);
        Message savedMessage4 = messageService.saveMessage(message4);

        assertEquals(message1.getSenderId(), savedMessage1.getSenderId());
        assertEquals(message1.getRecipientId(), savedMessage1.getRecipientId());
        assertEquals(message1.getCreateAt(), savedMessage1.getCreateAt());
        assertEquals(message1.getMessage(), savedMessage1.getMessage());

        assertEquals(message2.getSenderId(), savedMessage2.getSenderId());
        assertEquals(message2.getRecipientId(), savedMessage2.getRecipientId());
        assertEquals(message2.getCreateAt(), savedMessage2.getCreateAt());
        assertEquals(message2.getMessage(), savedMessage2.getMessage());

        assertEquals(message3.getSenderId(), savedMessage3.getSenderId());
        assertEquals(message3.getRecipientId(), savedMessage3.getRecipientId());
        assertEquals(message3.getCreateAt(), savedMessage3.getCreateAt());
        assertEquals(message3.getMessage(), savedMessage3.getMessage());

        assertEquals(message4.getSenderId(), savedMessage4.getSenderId());
        assertEquals(message4.getRecipientId(), savedMessage4.getRecipientId());
        assertEquals(message4.getCreateAt(), savedMessage4.getCreateAt());
        assertEquals(message4.getMessage(), savedMessage4.getMessage());

    }


    @Test
    @Order(3)
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

        List<MessageDTO> messageList = messageService.receiveAllMessage(senderId, recipientId);

        assertNotNull(messageList);
        assertEquals(testMessages.size(), messageList.size());

    }

    @Test
    @Order(3)
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

        List<MessageDTO> messageList = messageService.receiveAllMessage(senderId, recipientId);


        assertNotNull(messageList);
        assertEquals(testMessages.size(), messageList.size());

    }




}