package com.example.backend.conrollers;


import com.example.backend.DTO.MessageDTO;
import com.example.backend.model.Message;
import com.example.backend.repositories.MessageRepository;
import com.example.backend.services.props.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/saveMessage")
    public ResponseEntity<String> saveAllMessage(@Valid @RequestBody Message messages) {
        messages.setCreateAt(new Date());
        messageService.saveMessage(messages);
        return ResponseEntity.ok("Successfully");
    }

    @GetMapping("/receive-all-message")
    public List<MessageDTO> receiveAllMessage(@RequestParam("senderId") String senderId,
                                              @RequestParam("recipientId") String recipientId) {
        return messageService.receiveAllMessage(senderId, recipientId);
    }

}
