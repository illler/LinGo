package com.example.backend.DTO;

import com.example.backend.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

}
