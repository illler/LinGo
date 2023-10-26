package com.example.backend.services;

import com.example.backend.model.Friends;
import com.example.backend.model.User;
import com.example.backend.repositories.FriendsRepository;
import com.example.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;
    private final UserRepository userRepository;

    public void addNewFriend(String currentUser, String newFriend) {
        if (currentUser.equals(newFriend)) {
            throw new IllegalArgumentException("Нельзя добавить самого себя в друзья.");
        }

        User user = userRepository.findById(currentUser).orElseThrow();
        User newFriendUser = userRepository.findById(newFriend).orElseThrow();

        addFriend(user, newFriendUser);
        addFriend(newFriendUser, user);
    }

    @Transactional
    protected void addFriend(User user, User user2){
        Optional<Friends> friends = friendsRepository.findByUser(user);
        if (friends.isPresent()){
            friends.get().getFriends().add(user2.getId());
            friendsRepository.save(friends.get());
        }else {
            Friends newFriends = new Friends();
            newFriends.setUser(user);
            newFriends.setFriends(List.of(user2.getId()));
            friendsRepository.save(newFriends);
        }
    }

}
