package com.example.backend.services.props;

import com.example.backend.model.Friends;
import com.example.backend.model.User;
import com.example.backend.repositories.FriendsRepository;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class FriendsServiceTest {

    @Mock
    private FriendsRepository friendsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendsService friendsService;

    @Test
    void testAddNewFriend() {
        String currentUser = "user1";
        String newFriend = "user2";
        Friends friends = new Friends();
        friends.setUserId(currentUser);
        friends.setFriends(Set.of(newFriend));

        when(friendsRepository.findByUserId(currentUser)).thenReturn(Optional.of(friends));
        when(userRepository.findById(newFriend)).thenReturn(Optional.of(new User()));

        friendsService.addNewFriend(currentUser, newFriend);

        assertTrue(friends.getFriends().contains(newFriend));
    }

    @Test
    void testRetrieveAllUserFriends() {
        String userId = "user1";
        Friends friends = new Friends();
        friends.setUserId(userId);
        friends.setFriends(Set.of("user2", "user3"));

        when(friendsRepository.findByUserId(userId)).thenReturn(Optional.of(friends));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        assertNotNull(friendsService.retrieveAllUserFriends(userId));
        assertEquals(2, friendsService.retrieveAllUserFriends(userId).size());
    }

    @Test
    void testFriendsCheck() {
        String userId = "user1";
        String newFriendId = "user2";
        Friends friends = new Friends();
        friends.setUserId(userId);
        friends.setFriends(Set.of(newFriendId));

        when(friendsRepository.findByUserId(userId)).thenReturn(Optional.of(friends));

        assertTrue(friendsService.friendsCheck(userId, newFriendId));
    }

    @Test
    void testAddNewFriend_whenCurrentUserIdAndNewFriendIdIsEquals_throwsIllegalArgumentException(){
        String currentUser = "user1";
        String newFriend = "user1";
        Friends friends = new Friends();
        friends.setUserId(currentUser);
        friends.setFriends(Set.of(newFriend));


        assertThrows(IllegalArgumentException.class, ()->{
            friendsService.addNewFriend(currentUser, newFriend);
        }, "Current user id and friend id should not be same");

    }
}