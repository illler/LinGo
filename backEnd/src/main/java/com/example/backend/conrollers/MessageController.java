package com.example.backend.conrollers;


import com.example.backend.DTO.MessageDTO;
import com.example.backend.model.Message;
import com.example.backend.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;

    @PostMapping("/saveMessage")
    public void saveAllMessage(@RequestBody Message messages) {
        messages.setCreateAt(new Date());
        messageRepository.save(messages);
    }

    @GetMapping("/receive-all-message")
    public List<MessageDTO> receiveAllMessage(@RequestParam("senderId") String senderId,
                                              @RequestParam("recipientId") String recipientId) {
        List<MessageDTO> messageDTOS = new ArrayList<>();

        List<Message> messages = messageRepository
                .findAllBySenderIdAndRecipientIdOrderByCreateAt(senderId, recipientId);
        mapMessagesToDTO(messageDTOS, messages, "from");

        List<Message> recipientMessages = messageRepository
                .findAllBySenderIdAndRecipientIdOrderByCreateAt(recipientId, senderId);
        mapMessagesToDTO(messageDTOS, recipientMessages, "to");

        messageDTOS.sort(Comparator.comparing(MessageDTO::getCreateAt));

        return messageDTOS;
    }

    private void mapMessagesToDTO(List<MessageDTO> messageDTOS, List<Message> messages, String fromSelf) {
        for (Message message : messages) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage(message.getMessage());
            messageDTO.setFromSelf(fromSelf);
            messageDTO.setCreateAt(message.getCreateAt());
            messageDTOS.add(messageDTO);
        }
    }
}
