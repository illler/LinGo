package com.example.backend.services.props;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("MyUserDetailsService Tests")
class MyUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    DTOService dtoService;

    @InjectMocks
    MyUserDetailsService myUserDetailsService;

    User user;

    @BeforeEach
    void init(){
        user = User.builder()
                .id("1234")
                .firstname("Alex")
                .lastname("Pyatunin")
                .email("0250901@gmail.com")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    @DisplayName("Load User By Username: User Exists")
    void testLoadUserByUsername_whenUserIsExist_returnUserDetails(){
        String email = "0250901@gmail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));


        UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals(email, userDetails.getUsername(), "Username in UserDetails should match the expected email");


        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Load User By Username: User Does Not Exist")
    void testLoadUserByUsername_whenUserIsNotExist_throwsUsernameNotFoundException(){
        String email = "0260901@gmail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, ()->{
            myUserDetailsService.loadUserByUsername(email);
        });

        assertEquals("User not found", exception.getMessage(), "Exception message should match");

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Load User By ID: User Exists")
    void testLoadUserById_whenUserIsExist_returnUserDetails(){
        String id = "1234";

        when(userRepository.findById(id)).thenReturn(Optional.of(user));


        User userDetails = myUserDetailsService.loadUserById(id);

        assertNotNull(userDetails, "UserDetails should not be null");

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Load User By ID: User Does Not Exist")
    void testLoadUserById_whenUserIsNotExist_throwsUsernameNotFoundException(){
        String id = "1235";

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, ()->{
            myUserDetailsService.loadUserById(id);
        });

        assertEquals(exception.getMessage(), "User not found");

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Find User By Name or Email: Provided Name")
    void testFindUserByNameOrEmail_whenProvidedName_returnUserDetails(){
        String currentId = "4321";
        String name = "Alex";

        when(userRepository.findAllByEmailOrName(name, currentId)).thenReturn(List.of(user));


        List<User> userDetailsList = myUserDetailsService.findUserByNameOrEmail(name, currentId);
        assertNotNull(userDetailsList);
        assertEquals(1, userDetailsList.size(), "There should be exactly one user in the list");

        User userDetails = userDetailsList.get(0);
        assertEquals(name, userDetails.getFirstname(), "User's first name should match the expected name");

        verify(userRepository, times(1)).findAllByEmailOrName(name, currentId);
    }

    @Test
    @DisplayName("Find User By Name or Email: Provided Email")
    void testFindUserByNameOrEmail_whenProvidedEmail_returnUserDetails(){
        String currentId = "4321";
        String email = "0250901@gmail.com";

        when(userRepository.findAllByEmailOrName(email, currentId)).thenReturn(List.of(user));


        List<User> userDetailsList = myUserDetailsService.findUserByNameOrEmail(email, currentId);
        assertNotNull(userDetailsList);
        assertEquals(1, userDetailsList.size(), "There should be exactly one user in the list");

        User userDetails = userDetailsList.get(0);
        assertEquals(email, userDetails.getEmail(), "User's email should match the expected email");


        verify(userRepository, times(1)).findAllByEmailOrName(email, currentId);
    }

    @Test
    @DisplayName("Find User By Name or Email: Provided Email Equals Current User Email")
    void testFindUserByNameOrEmail_whenProvidedEmailEqualsCurrentUserEmail_returnEmptyList(){
        String currentId = "1234";
        String email = "0250901@gmail.com";

        when(userRepository.findAllByEmailOrName(email, currentId)).thenReturn(new ArrayList<>());

        List<User> userDetails = myUserDetailsService.findUserByNameOrEmail(email, currentId);
        assertEquals(new ArrayList<>(), userDetails, "The returned list should be empty");

        verify(userRepository, times(1)).findAllByEmailOrName(email, currentId);

    }


    @Test
    @DisplayName("Save User Info: All Details Provided")
    void testSaveUserInfo_whenAllDetailsIsProvided_returnSavedUser(){

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = myUserDetailsService.saveUserInfo(user);

        assertNotNull(savedUser, "Saved user should not be null");
        assertEquals(user.getFirstname(), savedUser.getFirstname(), "Saved user's firstname should match");
        assertEquals(user.getEmail(), savedUser.getEmail(), "Saved user's email should match");

    }

    @Test
    @DisplayName("Find All Users: Return List of Users")
    void testFindAllUsers_returnListOfUsers(){
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> userList = myUserDetailsService.findAllUsers();

        assertNotNull(userList, "User list should not be null");
        assertEquals(1, userList.size(), "User list should contain exactly one user");
        assertEquals(user, userList.get(0), "User in the list should match the expected user");

    }

    @Test
    @DisplayName("Find All Users By Their IDs: IDs Provided")
    void testFindAllUsersByTheirIds_whenIdsAreProvided_returnListOfUser() {
        UserDTO userDTO = UserDTO.builder()
                .firstname("Alex")
                .lastname("Pyatunin")
                .email("0250901@gmail.com")
                .role(Role.ADMIN)
                .build();

        String id = "1234";

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User userDetails = myUserDetailsService.loadUserById(id);

        when(dtoService.convertToUserDTO(any(User.class))).thenReturn(userDTO);

        List<UserDTO> usersInfo = myUserDetailsService.findAllUsersByTheirIds(List.of(id));

        assertNotNull(usersInfo, "UserDTO list should not be null");
        assertEquals(1, usersInfo.size(), "UserDTO list should contain exactly one user");

        UserDTO userDTOFromList = usersInfo.get(0);
        assertEquals(userDetails.getFirstname(), userDTOFromList.getFirstname(), "Firstname should match");
        assertEquals(userDetails.getLastname(), userDTOFromList.getLastname(), "Lastname should match");
        assertEquals(userDetails.getEmail(), userDTOFromList.getEmail(), "Email should match");
        assertEquals(userDTO.getRole(), userDTOFromList.getRole(), "Role should match");
    }

}