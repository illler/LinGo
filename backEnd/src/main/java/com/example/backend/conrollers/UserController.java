package com.example.backend.conrollers;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.User;
import com.example.backend.repositories.TokenRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.MyUserDetailsService;
import com.example.backend.services.TokenService;
import com.example.backend.token.Token;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final MyUserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;


    @GetMapping("/getCurrentUser")
    public UserDTO getCurrentRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return convertToPersonDTO(user);
        } else {
            return null;
        }
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

    public UserDTO convertToPersonDTO(User person) {
        return modelMapper.map(person, UserDTO.class);
    }

    public User convertToPerson(UserDTO personDTO) {
        return modelMapper.map(personDTO, User.class);
    }

}
