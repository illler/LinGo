package com.example.backend.conrollers;

import com.example.backend.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private static final Map<String, String> onlineUsers = new ConcurrentHashMap<>();

    @MessageMapping("/send-msg")
    @SendTo("/chat")
    public Message sendMessage(@Payload Message message) {
        // Логика обработки сообщения
        simpMessagingTemplate.convertAndSend("/chat", message);
        return message;
    }

    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload Message message) {
        // Логика для отправки приватного сообщения
        String recipientSocketId = onlineUsers.get(message.getRecipientId());
        if (recipientSocketId != null) {
            simpMessagingTemplate.convertAndSendToUser(recipientSocketId, "/private", message);
        }
    }
}