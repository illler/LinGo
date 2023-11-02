package com.example.backend.conrollers;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.EmailDetails;
import com.example.backend.model.MailRequest;
import com.example.backend.model.MailType;
import com.example.backend.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody MailRequest mailRequest) {
        emailService.sendFriendMail(mailRequest.getUserFrom(), mailRequest.getUserTo());
        return "Письмо успешно отправлено!";
    }

}
