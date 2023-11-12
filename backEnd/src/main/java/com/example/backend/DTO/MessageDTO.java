package com.example.backend.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDTO {

    private String userId;
    private String message;
    private Date createAt;
}
