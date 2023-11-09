package com.example.backend.services;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.User;

public interface EmailService {

    void sendPasswordRecoveryMail(User user);

    void sendRegistrationMail(User user);

    void sendFriendMail(UserDTO userFrom, UserDTO userTo);


}
