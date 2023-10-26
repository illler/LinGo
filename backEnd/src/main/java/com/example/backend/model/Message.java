package com.example.backend.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String senderName;
    private String receiverName;
    private String senderId;
    private String recipientId;
    private String message;
    private String date;
    private Status status;
}