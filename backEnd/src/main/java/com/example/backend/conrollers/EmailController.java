package com.example.backend.conrollers;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.EmailDetails;
import com.example.backend.model.MailRequest;
import com.example.backend.model.MailType;
import com.example.backend.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody MailRequest mailRequest) {
        emailService.sendFriendMail(mailRequest.getUserFrom(), mailRequest.getUserTo());
        return "Письмо успешно отправлено!";
    }

}
