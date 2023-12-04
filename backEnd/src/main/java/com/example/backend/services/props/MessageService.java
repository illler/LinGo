package com.example.backend.services.props;

import com.example.backend.DTO.MessageDTO;
import com.example.backend.model.Message;
import com.example.backend.repositories.MessageRepository;
import com.example.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    @Transactional
    public Message saveMessage(Message message){
        message.setCreateAt(new Date());
        messageRepository.save(message);
        return message;
    }

    public List<Message> receiveAllMessage(String sender, String recipient) {
        List<Message> senderMessages = new ArrayList<>(messageRepository
                .findAllBySenderIdAndRecipientIdOrderByCreateAt(sender, recipient));

        if (!sender.equals(recipient)) {
            List<Message> recipientMessages = new ArrayList<>(messageRepository
                    .findAllBySenderIdAndRecipientIdOrderByCreateAt(recipient, sender));
            senderMessages.addAll(recipientMessages);
        }

        senderMessages.sort(Comparator.comparing(Message::getCreateAt));

        return senderMessages;
    }


//    private void mapMessagesToDTO(List<MessageDTO> messageDTOS, List<Message> messages, String fromSelf) {
//        for (Message message : messages) {
//            MessageDTO messageDTO = new MessageDTO();
//            messageDTO.setMessage(message.getMessage());
//            messageDTO.setUserId(fromSelf);
//            messageDTO.setCreateAt(message.getCreateAt());
//            messageDTOS.add(messageDTO);
//        }
//    }

    public Set<String> receivingAllInterlocutorsId(String currentUserId) {
        Set<String> interlocutors = messageRepository.findAllUsersIdWhoDoWeHaveCorrespondenceWith(currentUserId);
        Set<String> interlocutor2 = messageRepository.findAllUsersIdWhoDoWriteUs(currentUserId);
        interlocutors.addAll(interlocutor2);
        return interlocutors;
    }
}
