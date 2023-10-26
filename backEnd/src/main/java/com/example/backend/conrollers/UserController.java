package com.example.backend.conrollers;

import com.example.backend.model.User;
import com.example.backend.repositories.TokenRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.MyUserDetailsService;
import com.example.backend.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final MyUserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;


    @GetMapping("/getCurrentUser")
    public ResponseEntity<User> getCurrentUser(@RequestHeader(name = "Authorization") String token){
        return ResponseEntity.ok(tokenRepository.findByToken(token.substring(7)).orElse(null).getUser());
    }


    @PutMapping("/profile/saveUserInfo")
    public ResponseEntity<User> saveUserInfo(@RequestBody User user){
        userDetailsService.saveNewOrUpdateExistingUser(user);
        return ResponseEntity.ok(user);
    }

//    public ResponseEntity<String> addFriend(@RequestParam String currentUserId,
//                                            @RequestParam String newFriendId){
//
//    }

}
