package com.example.backend.services.props;

import com.example.backend.auth.AuthenticationRequest;
import com.example.backend.auth.AuthenticationResponse;
import com.example.backend.auth.RegisterRequest;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.repositories.TokenRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.impl.EmailServiceImpl;
import com.example.backend.token.Token;
import com.example.backend.token.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest {

    @Mock
    UserRepository repository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtService jwtService;

    @Mock
    TokenRepository tokenRepository;

    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    EmailServiceImpl emailService;

    @InjectMocks
    AuthenticationService authenticationService;

    User user;

    Token token;

    @BeforeEach
    void init(){
         user = User.builder()
                .id("1234")
                .firstname("Alex")
                .lastname("Pyatunin")
                .email("0250901@gmail.com")
                .phoneNumber("89677873636")
                .password("1234")
                .role(Role.USER)
                .build();
         token = Token.builder()
                .user(user)
                .token("123412341234")
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
    }


    @Test
    @DisplayName("Test register")
    void testRegister_whenUserTryToRegister_returnTokenAndUserId(){


        RegisterRequest request = RegisterRequest.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
        when(passwordEncoder.encode(any(String.class))).thenReturn("password");
        when(repository.save(any(User.class))).thenReturn(user);
        when(tokenRepository.save(any(Token.class))).thenReturn(token);
        when(jwtService.generateToken(any(User.class))).thenReturn("123412341234");

        AuthenticationResponse authenticationResponse = authenticationService.register(request);

        assertNotNull(authenticationResponse, "Response should not be null");
        assertEquals(user.getId(), authenticationResponse.getId(), "Id before and after registration must be the same");
        assertEquals(token.getToken(), authenticationResponse.getToken(), "Token before and after registration must be the same");
        assertNotEquals(user.getPassword(), "password", "Password before and after registration should not be the same");
    }

    @Test
    @DisplayName("Test login")
    void testAuthenticate_whenUserTryToLogin_returnTokenAndUserId(){
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("0250901@gmail.com")
                .password("1234")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(tokenRepository.saveAll(anyList())).thenReturn(List.of(token));

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("123412341234");
        when(tokenRepository.save(any(Token.class))).thenReturn(token);
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(List.of(token));


        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);

        assertNotNull(authenticationResponse, "Response should not be null");
        assertEquals(user.getId(), authenticationResponse.getId(), "Id before and after login must be the same");
        assertEquals(token.getToken(), authenticationResponse.getToken(), "Token before and after login must be the same");

    }

    @Test
    @DisplayName("Test check temporary password if all is ok")
    void testCheckTemporaryPassword_whenUserPutRightCode_returnTrueOrFalse(){
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("0250901@gmail.com")
                .password("1234")
                .build();
        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        boolean isValidTmpPassword = authenticationService.checkTemporaryPassword(request);

        assertTrue(isValidTmpPassword, "Password must match with code");

    }

    @Test
    @DisplayName("Test check temporary password if user make a mistake")
    void testCheckTemporaryPassword_whenUserPutDifferentCode_returnTrueOrFalse(){
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("0250901@gmail.com")
                .password("12345")
                .build();
        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        boolean isValidTmpPassword = authenticationService.checkTemporaryPassword(request);

        assertFalse(isValidTmpPassword, "The password must not match the code");

    }

    @Test
    @DisplayName("Test update password function")
    void testUpdatePassword(){
        AuthenticationRequest request = new AuthenticationRequest("0250901@gmail.com", "newPassword");

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedNewPassword");
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = authenticationService.updatePassword(request);

        assertEquals("1234", updatedUser.getId());
        assertEquals("0250901@gmail.com", updatedUser.getEmail());
        assertEquals("encodedNewPassword", updatedUser.getPassword());


        verify(repository, times(1)).findByEmail("0250901@gmail.com");
        verify(passwordEncoder,times(1)).encode("newPassword");
        verify(repository, times(1)).save(any(User.class));
    }

}