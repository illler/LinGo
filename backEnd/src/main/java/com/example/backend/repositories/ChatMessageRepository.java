package com.example.backend.repositories;

import com.example.backend.model.ChatMessage;
import com.example.backend.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);

    @Query(nativeQuery = true, value = "UPDATE chat_message  SET status = :status WHERE sender_id = :senderId AND recipient_id = :recipientId")
    void updateStatusMessage(MessageStatus status, String senderId, String recipientId);
}
