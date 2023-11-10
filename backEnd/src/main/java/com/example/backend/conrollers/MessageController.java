package com.example.backend.conrollers;


import com.example.backend.DTO.MessageDTO;
import com.example.backend.model.Message;
import com.example.backend.repositories.MessageRepository;
import com.example.backend.services.props.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/saveMessage")
    public void saveAllMessage(@RequestBody Message messages) {
        messages.setCreateAt(new Date());
        messageService.saveMessage(messages);
    }

    @GetMapping("/receive-all-message")
    public List<MessageDTO> receiveAllMessage(@RequestParam("senderId") String senderId,
                                              @RequestParam("recipientId") String recipientId) {
        return messageService.receiveAllMessage(senderId, recipientId);
    }

}
