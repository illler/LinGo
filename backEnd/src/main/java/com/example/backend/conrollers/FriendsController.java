package com.example.backend.conrollers;


import com.example.backend.services.props.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendsController {


    private final FriendsService friendsService;


    @PostMapping("/addFriends")
    public String addFriend(@RequestParam String currentUserId,
                            @RequestParam String newFriendId){
        friendsService.addNewFriend(currentUserId, newFriendId);
        return "Друзья добавлены";
    }

//    @PostMapping("/sendRequest")
//    public

}
