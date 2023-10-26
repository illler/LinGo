package com.example.backend.services;

import com.example.backend.model.User;
import com.example.backend.repositories.TokenRepository;
import com.example.backend.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public User getUserByToken(String tokenValue){
        tokenValue = tokenValue.substring(7);
        Token token = tokenRepository.findByToken(tokenValue).orElse(null);
        if (token!=null) {
            return token.getUser();
        }
        else return null;
    }
}
