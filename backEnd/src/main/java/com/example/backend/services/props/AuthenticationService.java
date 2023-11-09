package com.example.backend.services.props;

import com.example.backend.DTO.AuthDTO;
import com.example.backend.auth.AuthenticationRequest;
import com.example.backend.auth.AuthenticationResponse;
import com.example.backend.auth.RegisterRequest;
import com.example.backend.config.JwtService;
import com.example.backend.model.User;
import com.example.backend.repositories.TokenRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.impl.EmailServiceImpl;
import com.example.backend.token.Token;
import com.example.backend.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.backend.model.Role.USER;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;
    private final EmailServiceImpl emailService;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = repository.save(user);
        String id = user.getId();
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        emailService.sendRegistrationMail(savedUser);
        return AuthenticationResponse.builder()
                .id(id)
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        String id = user.getId();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .id(id)
                .token(jwtToken)
                .build();
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserToken(User user){
        var validToken = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validToken.isEmpty()){
            return;
        }
        validToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validToken);
    }

    public boolean checkTemporaryPassword(AuthDTO authDTO) {
        User oldUser = repository.findByEmail(authDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return (oldUser.getPassword().equals(authDTO.getPassword()));
    }

    @Transactional
    public void updatePassword(AuthDTO authDTO){
        User oldUser = repository.findByEmail(authDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        oldUser.setPassword(passwordEncoder.encode(authDTO.getPassword()));
        repository.save(oldUser);
    }
}
