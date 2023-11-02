package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String senderName;
    private String receiverName;
    private String senderId;
    private String recipientId;
    private String message;
    private Status status;

    @Column(name = "create_at")
    private Date createAt;
}