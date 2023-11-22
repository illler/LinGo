package com.example.backend.services.props;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.Role;
import com.example.backend.model.User;
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

        assertEquals(expectedUserDTO.getFirstname(), convertedUser.getFirstname());
        assertEquals(expectedUserDTO.getLastname(), convertedUser.getLastname());
        assertEquals(expectedUserDTO.getEmail(), convertedUser.getEmail());
        assertEquals(expectedUserDTO.getRole(), convertedUser.getRole());
    }


    @Test
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

        assertEquals(userDTO.getFirstname(), convertedUserDTO.getFirstname());
        assertEquals(userDTO.getLastname(), convertedUserDTO.getLastname());
        assertEquals(userDTO.getEmail(), convertedUserDTO.getEmail());
        assertEquals(userDTO.getRole(), convertedUserDTO.getRole());
    }
}