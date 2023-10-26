package com.example.backend.services;

import com.example.backend.model.User;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class MyMyUserDetailsServiceTest {


    private MyUserDetailsService userDetailsService;


    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userDetailsService = new MyUserDetailsService(userRepository);
    }

    @Test
    public void testLoadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        String username = "test@example.com";
        User user = User.builder().build();
        Mockito.when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails loadedUser = userDetailsService.loadUserByUsername(username);

        // Assert
        assertEquals(user, loadedUser);
    }


    @Test
    public void testLoadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        String username = "nonexistent@example.com";
        Mockito.when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }

}
