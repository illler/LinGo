package com.example.backend.model;

import com.example.backend.DTO.UserDTO;
import lombok.Data;

@Data
public class MailRequest {

    private UserDTO userTo;
    private UserDTO userFrom;

}
