package com.example.backend.services.props;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.Friends;
import com.example.backend.model.User;
import com.example.backend.repositories.FriendsRepository;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
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
    @DisplayName("Add New Friend: When Valid User and New Friend Provided, Then Friend Should Be Added")
    void testAddNewFriend() {
        String currentUser = "user1";
        String newFriend = "user2";
        Friends friends = new Friends();
        friends.setUserId(currentUser);
        friends.setFriends(Set.of(newFriend));

        when(friendsRepository.findByUserId(currentUser)).thenReturn(Optional.of(friends));
        when(userRepository.findById(newFriend)).thenReturn(Optional.of(new User()));

        friendsService.addNewFriend(currentUser, newFriend);

        assertTrue(friends.getFriends().contains(newFriend),
                "The new friend should be added to the friends list");
    }

    @Test
    @DisplayName("Retrieve All User Friends: When Valid User Provided, Then Return List of User Friends")
    void testRetrieveAllUserFriends() {
        String userId = "user1";
        Friends friends = new Friends();
        friends.setUserId(userId);
        friends.setFriends(Set.of("user2", "user3"));

        when(friendsRepository.findByUserId(userId)).thenReturn(Optional.of(friends));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        List<User> userFriends = friendsService.retrieveAllUserFriends(userId);

        assertNotNull(userFriends, "User friends list should not be null");
        assertEquals(2, userFriends.size(), "The number of user friends should be 2");
    }

    @Test
    @DisplayName("Add New Friend: When Current User ID and New Friend ID Are Equal, Then Throw IllegalArgumentException")
    void testAddNewFriend_whenCurrentUserIdAndNewFriendIdIsEquals_throwsIllegalArgumentException(){
        String currentUser = "user1";
        String newFriend = "user1";
        Friends friends = new Friends();
        friends.setUserId(currentUser);
        friends.setFriends(Set.of(newFriend));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            friendsService.addNewFriend(currentUser, newFriend);
        }, "An IllegalArgumentException should be thrown");

        assertEquals("Current user id and friend id should not be the same", exception.getMessage(),
                "Exception message should match");
    }

    @Test
    @DisplayName("Check Friends: When Valid User and Friend Provided, Then Return True If Friends, False Otherwise")
    void testFriendsCheck() {
        String currentUser = "user1";
        String newFriend = "user2";
        Friends friends = new Friends();
        friends.setUserId(currentUser);
        friends.setFriends(Set.of(newFriend));

        when(friendsRepository.findByUserId(currentUser)).thenReturn(Optional.of(friends));

        assertTrue(friendsService.friendsCheck(currentUser, newFriend),
                "The method should return true since 'user2' is a friend of 'user1'");
    }

    @Test
    @DisplayName("Check Friends: When Valid User and Non-Friend Provided, Then Return False")
    void testFriendsCheck_whenNonFriendProvided_returnFalse() {
        String currentUser = "user1";
        String nonFriend = "user3";
        Friends friends = new Friends();
        friends.setUserId(currentUser);
        friends.setFriends(Set.of("user2"));

        when(friendsRepository.findByUserId(currentUser)).thenReturn(Optional.of(friends));

        assertFalse(friendsService.friendsCheck(currentUser, nonFriend),
                "The method should return false since 'user3' is not a friend of 'user1'");
    }

    @Test
    @DisplayName("Delete Friend: When Valid User and Friend Provided, Then Remove Friend and Return True")
    void testDeleteFriend() {
        String currentUser = "user1";
        String friendIdToRemove = "user2";
        Friends friends = new Friends();
        friends.setUserId(currentUser);
        friends.setFriends(new HashSet<>(Set.of("user2", "user3")));

        when(friendsRepository.findByUserId(currentUser)).thenReturn(Optional.of(friends));

        assertTrue(friendsService.deleteFriend(currentUser, friendIdToRemove),
                "The method should return true since 'user2' is removed from the friends list");
        assertFalse(friends.getFriends().contains(friendIdToRemove),
                "The friend 'user2' should be removed from the friends list");
    }

    @Test
    @DisplayName("Delete Friend: When Non-Friend Provided, Then Return False")
    void testDeleteFriend_whenNonFriendProvided_returnFalse() {
        String currentUser = "user1";
        String nonFriendId = "user3";
        Friends friends = new Friends();
        friends.setUserId(currentUser);
        friends.setFriends(new HashSet<>(Set.of("user2")));

        when(friendsRepository.findByUserId(currentUser)).thenReturn(Optional.of(friends));

        assertFalse(friendsService.deleteFriend(currentUser, nonFriendId),
                "The method should return false since 'user3' is not in the friends list");
    }

}