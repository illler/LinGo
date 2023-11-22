package com.example.backend.conrollers;


import com.example.backend.DTO.MessageDTO;
import com.example.backend.DTO.UserDTO;
import com.example.backend.model.Message;
import com.example.backend.repositories.MessageRepository;
import com.example.backend.services.props.MessageService;
import com.example.backend.services.props.MyUserDetailsService;
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
    private final MyUserDetailsService myUserDetailsService;

    @PostMapping("/saveMessage")
    public ResponseEntity<String> saveMessage(@Valid @RequestBody Message messages) {
        messages.setCreateAt(new Date());
        messageService.saveMessage(messages);
        return ResponseEntity.ok("Successfully");
    }

    @GetMapping("/receive-all-message")
    public ResponseEntity<List<MessageDTO>> receiveAllMessage(@RequestParam("senderId") String senderId,
                                              @RequestParam("recipientId") String recipientId) {
        return ResponseEntity.ok(messageService.receiveAllMessage(senderId, recipientId));
    }

    @GetMapping("/receiving-all-correspondence")
    public ResponseEntity<List<UserDTO>> receivingAllCorrespondence(@RequestParam("senderId") String currentUserId){
        List<String> usersIds = messageService.receivingAllInterlocutorsId(currentUserId);
        return ResponseEntity.ok(myUserDetailsService.findAllUsersByTheirIds(usersIds));
    }


}
