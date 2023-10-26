package com.example.backend.DTO;

import com.example.backend.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

}
