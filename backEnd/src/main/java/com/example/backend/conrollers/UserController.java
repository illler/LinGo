package com.example.backend.conrollers;

import com.example.backend.model.User;
import com.example.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PutMapping("/saveUserInfo")
    public ResponseEntity<User> saveUserInfo( @RequestBody User user){
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
