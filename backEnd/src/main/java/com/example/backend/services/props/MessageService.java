package com.example.backend.services.props;

import com.example.backend.DTO.MessageDTO;
import com.example.backend.model.Message;
import com.example.backend.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public void saveMessage(Message message){
        messageRepository.save(message);
    }

    public List<MessageDTO> receiveAllMessage(String sender, String recipient) {
        List<MessageDTO> messageDTOS = new ArrayList<>();
        List<Message> recipientMessages = new ArrayList<>();
        List<Message> messages = messageRepository
                .findAllBySenderIdAndRecipientIdOrderByCreateAt(sender, recipient);
        mapMessagesToDTO(messageDTOS, messages, sender);

        if (!sender.equals(recipient)) {
             recipientMessages = messageRepository
                    .findAllBySenderIdAndRecipientIdOrderByCreateAt(recipient, sender);
        }
        mapMessagesToDTO(messageDTOS, recipientMessages, recipient);
        System.out.println(messages);
        System.out.println("-----------------------");
        System.out.println(recipientMessages);
        messageDTOS.sort(Comparator.comparing(MessageDTO::getCreateAt));

        return messageDTOS;
    }

    private void mapMessagesToDTO(List<MessageDTO> messageDTOS, List<Message> messages, String fromSelf) {
        for (Message message : messages) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage(message.getMessage());
            messageDTO.setUserId(fromSelf);
            messageDTO.setCreateAt(message.getCreateAt());
            messageDTOS.add(messageDTO);
        }
    }
}
