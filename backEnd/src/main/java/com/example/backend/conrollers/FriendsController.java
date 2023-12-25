package com.example.backend.conrollers;


import com.example.backend.DTO.UserDTO;
import com.example.backend.model.User;
import com.example.backend.services.props.DTOService;
import com.example.backend.services.props.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendsController {


    private final FriendsService friendsService;
    private final DTOService dtoService;


    @PostMapping("/addFriends")
    public String addFriend(@RequestParam String currentUserId,
                            @RequestParam String newFriendId){
        friendsService.addNewFriend(currentUserId, newFriendId);
        return "Друзья добавлены";
    }

    @GetMapping("/friendsCheck")
    public Boolean friendsCheck(@RequestParam String currentUserId,
                               @RequestParam String newFriendId){
        return friendsService.friendsCheck(currentUserId, newFriendId);
    }

    @GetMapping("/retrieveAllFriends")
    public List<UserDTO> retrieveAllFriends(@RequestParam String userId){
        List<User> friends = friendsService.retrieveAllUserFriends(userId);
        if (friends == null){
            return new ArrayList<>();
        }
        return friends
                .stream()
                .map(dtoService::convertToUserDTO)
                .toList();
    }

    @DeleteMapping("/deleteFriend")
    public ResponseEntity<Boolean> deleteFriend(@RequestParam String currentUserId,
                                                @RequestParam String friendId){
        return ResponseEntity.ok(friendsService.deleteFriend(currentUserId, friendId));
    }

    

}
