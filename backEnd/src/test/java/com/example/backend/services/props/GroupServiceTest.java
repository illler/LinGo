package com.example.backend.services.props;

import com.example.backend.model.Group;
import com.example.backend.repositories.GroupRepository;
import com.example.backend.repositories.UserGroupRepository;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private UserRepository userRepository;
    String groupId;
    String userId;
    Group group;

    @BeforeEach
    void init(){
         groupId = "someGroupId";
         userId = "someUserId";

         group = Group.builder()
                .groupId(groupId)
                .groupName("testGroupName")
                .ownerId(userId)
                .build();
    }

    @Test
    @DisplayName("Test return all group methods function")
    void testReturnAllGroupsMethod_whenProvidedCorrectUserId_thenReturnListOfGroups(){
        String groupId = "someGroupId";
        String userId = "someUserId";

        Group group = Group.builder()
                .groupId(groupId)
                .groupName("testGroupName")
                .ownerId(userId)
                .build();

        when(userGroupRepository.findGroupIdsByUserId(anyString())).thenReturn(List.of(groupId));
        when(groupRepository.findAllById(List.of(groupId))).thenReturn(List.of(group));

        List<Group> groupList = groupService.getAllGroups(userId);
        assertNotNull(groupList, "List of group should not return null");
        assertEquals(groupList.get(0).getGroupId(), group.getGroupId(), "Groups id must be the same");
    }


    @Test
    @DisplayName("Test add new group function")
    void testAddNewGroup(){

        List<String> usersId = new ArrayList<>();
        usersId.add("userId1");
        usersId.add("userId2");
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        groupService.addNewGroup(group, usersId);

        verify(groupRepository, times(1)).save(group);

        verify(userGroupRepository, times(1)).saveAll(anyList());

    }

    @Test
    @DisplayName("Test add new user to group")
    void testAddNewUserToGroup(){

        List<String> usersId = new ArrayList<>();
        usersId.add("userId1");
        usersId.add("userId2");

        groupService.addNewUserToGroup(group, usersId);

        verify(userGroupRepository, times(1)).saveAll(anyList());

    }

}