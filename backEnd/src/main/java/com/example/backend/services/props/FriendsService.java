package com.example.backend.services.props;

import com.example.backend.model.Friends;
import com.example.backend.model.User;
import com.example.backend.repositories.FriendsRepository;
import com.example.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;
    private final UserRepository userRepository;

    public void addNewFriend(String currentUser, String newFriend) {
        if (currentUser.equals(newFriend)) {
            throw new IllegalArgumentException("Current user id and friend id should not be the same");
        }
        addFriend(currentUser, newFriend);
        addFriend(newFriend, currentUser);
    }

    @Transactional
    protected void addFriend(String currentUser, String newFriendId) {
        Optional<Friends> friends = friendsRepository.findByUserId(currentUser);
        if (friends.isPresent()) {
            Set<String> currentFriends = new HashSet<>(friends.get().getFriends());
            currentFriends.add(newFriendId);
            friends.get().setFriends(currentFriends);
            friendsRepository.save(friends.get());
        } else {
            Friends newFriends = new Friends();
            newFriends.setUserId(currentUser);
            newFriends.setFriends(Set.of(newFriendId));
            friendsRepository.save(newFriends);
        }
    }


    public List<User> retrieveAllUserFriends(String userId) {
        Optional<Friends> friendIds = friendsRepository.findByUserId(userId);
        return friendIds.map(friends -> friends.getFriends().stream()
                .map(s -> userRepository.findById(s).orElse(null))
                .toList()).orElse(null);
    }


    public Boolean friendsCheck(String userId, String newFriendId) {
        Optional<Friends> friends = friendsRepository.findByUserId(userId);
        return friends.map(value -> value.getFriends().contains(newFriendId))
                .orElse(null);
    }

    @Transactional
    public Boolean deleteFriend(String currentUserId, String friendId) {
        Friends user = friendsRepository.findByUserId(currentUserId).orElseThrow();
        Set<String> friends = user.getFriends();
        return friends.remove(friendId);
    }
}
