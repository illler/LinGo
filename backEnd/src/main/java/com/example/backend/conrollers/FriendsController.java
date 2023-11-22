package com.example.backend.conrollers;


import com.example.backend.DTO.UserDTO;
import com.example.backend.services.props.DTOService;
import com.example.backend.services.props.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        return friendsService.retrieveAllUserFriends(userId)
                .stream()
                .map(dtoService::convertToUserDTO)
                .toList();
    }

}
