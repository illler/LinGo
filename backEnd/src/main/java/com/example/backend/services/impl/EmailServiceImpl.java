package com.example.backend.services.impl;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.MailType;
import com.example.backend.model.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.EmailService;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Annotation
@Service
@RequiredArgsConstructor
// Class
// Implementing EmailService interface
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private final Configuration configuration;

    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String sender;



//    @SneakyThrows
//    public String sendFriendMail(EmailDetails details) {
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
//        helper.setSubject("Запрос в друзя");
//        try {
//
//            SimpleMailMessage mailMessage
//                    = new SimpleMailMessage();
//
//            mailMessage.setFrom(sender);
//            mailMessage.setTo(details.getRecipient());
//            mailMessage.setText(details.getMsgBody());
//            mailMessage.setSubject(details.getSubject());
//
//            javaMailSender.send(mailMessage);
//            return "Mail Sent Successfully...";
//        }
//
//        catch (Exception e) {
//            return "Error while Sending Mail";
//        }
//    }

    @SneakyThrows
    public void sendFriendMail(UserDTO userFrom, UserDTO userTo) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject("New Friend Request");
        helper.setTo(userTo.getEmail());
        String emailContent = getFriendRequestEmailContent(userFrom, userTo);
        helper.setText(emailContent, true);
        javaMailSender.send(message);
    }



    @SneakyThrows
    public void sendRegistrationMail(User user) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject("You register on LinGo");
        helper.setTo(user.getEmail());
        String emailContent = getRegistrationEmailContent(user);
        helper.setText(emailContent, true);
        javaMailSender.send(message);
    }



    @SneakyThrows
    public void sendPasswordRecoveryMail(User user) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject("LinGo Auth");
        helper.setTo(user.getEmail());
        String emailContent = getRecoveryPasswordEmailContent(user);
        helper.setText(emailContent, true);
        javaMailSender.send(message);
    }

    @SneakyThrows
    private String getRecoveryPasswordEmailContent(User user) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        String newPassword = String.format("%06d", number);
        user.setPassword(newPassword);
        userRepository.save(user);

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getFirstname());
        model.put("newPassword", newPassword);
        configuration.getTemplate("recovery.ftlh")
                .process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    @SneakyThrows
    private String getFriendRequestEmailContent(UserDTO userFrom, UserDTO userTo) {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("userFrom", userFrom.getFirstname());
        configuration.getTemplate("friend.ftlh")
                .process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    @SneakyThrows
    private String getRegistrationEmailContent(User user) {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user.getFirstname());
        configuration.getTemplate("registration.ftlh")
                .process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

}