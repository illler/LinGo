package com.example.backend.services.props;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class DTOServiceTest {


    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DTOService dtoService;

    @Test
    @DisplayName("Convert To User DTO: When User Provided, Then Return UserDTO")
    void testConvertToUserDTO() {
        User user = User.builder()
                .firstname("Alex")
                .lastname("Pyatunin")
                .phoneNumber("89099998121")
                .email("tezerakt10@gmail.com")
                .role(Role.USER)
                .build();

        UserDTO expectedUserDTO = new UserDTO("id1",
                "Alex",
                "Pyatunin",
                "tezerakt10@gmail.com",
                "89094563321",
                Role.USER);

        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedUserDTO);

        UserDTO convertedUser = dtoService.convertToUserDTO(user);

        assertEquals(expectedUserDTO.getFirstname(), convertedUser.getFirstname(),
                "First name does not match");
        assertEquals(expectedUserDTO.getLastname(), convertedUser.getLastname(),
                "Last name does not match");
        assertEquals(expectedUserDTO.getEmail(), convertedUser.getEmail(),
                "Email does not match");
        assertEquals(expectedUserDTO.getRole(), convertedUser.getRole(),
                "Role does not match");
    }


    @Test
    @DisplayName("Convert To User: When UserDTO Provided, Then Return User")
    void testConvertToUser() {
        User expectedUser = User.builder()
                .firstname("Alex")
                .lastname("Pyatunin")
                .phoneNumber("89099998121")
                .email("tezerakt10@gmail.com")
                .role(Role.USER)
                .build();

        UserDTO userDTO = new UserDTO("id1",
                "Alex",
                "Pyatunin",
                "tezerakt10@gmail.com",
                "89094563321",
                Role.USER);

        when(modelMapper.map(userDTO, User.class)).thenReturn(expectedUser);

        User convertedUserDTO = dtoService.convertToUser(userDTO);

        assertEquals(userDTO.getFirstname(), convertedUserDTO.getFirstname(),
                "First name does not match");
        assertEquals(userDTO.getLastname(), convertedUserDTO.getLastname(),
                "Last name does not match");
        assertEquals(userDTO.getEmail(), convertedUserDTO.getEmail(),
                "Email does not match");
        assertEquals(userDTO.getRole(), convertedUserDTO.getRole(),
                "Role does not match");
    }
}