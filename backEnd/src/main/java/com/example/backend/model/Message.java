package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotEmpty(message = "Sender id should not be empty")
    private String senderId;

    @NotEmpty(message = "Recipient id should not be empty")
    private String recipientId;

    @Column(name = "original-message")
    private String originalMessage;

    @Column(name = "translated-message")
    private String translatedMessage;

    @Column(name = "create_at")
    @JsonIgnore
    private Date createAt;
}
