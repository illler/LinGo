package com.example.backend.conrollers;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.props.DTOService;
import com.example.backend.services.props.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final MyUserDetailsService userDetailsService;
    private final DTOService dtoService;


    @GetMapping("/getCurrentUser")
    public UserDTO getCurrentRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return dtoService.convertToPersonDTO(user);
        } else {
            return null;
        }
    }

    @PutMapping("/profile/saveUserInfo")
    public ResponseEntity<User> saveUserInfo(@RequestBody User user) {
        userDetailsService.saveNewOrUpdateExistingUser(user);
        return ResponseEntity.ok(user);
    }


    @GetMapping("/search")
    public List<UserDTO> searchForFriends(@RequestParam String pattern) {
        List<User> userList = userDetailsService.findUserByNameOrEmail(pattern);
        return userList.stream().map(dtoService::convertToPersonDTO).toList();
    }

}
