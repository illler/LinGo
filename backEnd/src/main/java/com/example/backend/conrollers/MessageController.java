package com.example.backend.conrollers;


import com.example.backend.model.Message;
import com.example.backend.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;


    @PostMapping("/saveMessage")
    public void saveAllMessage(@RequestBody Message messages){
        messageRepository.save(messages);
    }

    @GetMapping("/receive-all-message")
    public List<Message> receiveAllMessage(@RequestBody Map<String, String> payload){
        return messageRepository.findAllBySenderIdAndRecipientId(payload.get("senderId"), payload.get("recipientId"));
    }
}