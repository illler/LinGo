package com.example.backend.conrollers;

import com.example.backend.model.Message;
import com.example.backend.model.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.props.MessageService;
import com.example.backend.services.props.MyUserDetailsService;
import com.example.backend.services.props.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    private final TranslationService translationService;

    private final MyUserDetailsService myUserDetailsService;
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        messageService.saveMessage(message);
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        User user = myUserDetailsService.loadUserById(message.getRecipientId());
        message.setTranslatedMessage(translationService.translateTexts(
                List.of(message.getOriginalMessage()),
                        user.getLang())
                .get(0));
        messageService.saveMessage(message);
        simpMessagingTemplate.convertAndSendToUser(message.getRecipientId(),"/private",message);
        return message;
    }
}
