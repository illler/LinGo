package com.example.backend.services.props;

import com.example.backend.model.Group;
import com.example.backend.model.User;
import com.example.backend.model.UserGroup;
import com.example.backend.repositories.GroupRepository;
import com.example.backend.repositories.UserGroupRepository;
import com.example.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final UserGroupRepository userGroupRepository;

    private final UserRepository userRepository;


    public List<Group> getAllGroups(String userId) {
        List<String> groupsId = userGroupRepository.findGroupIdsByUserId(userId);
        return groupRepository.findAllById(groupsId);
    }

    @Transactional
    public void addNewGroup(Group group, List<String> usersId) {
        Group savedGroup = groupRepository.save(group);
        String groupId = savedGroup.getGroupId();

        usersId.add(group.getOwnerId());

        List<UserGroup> userGroups = saveUserGroup(usersId, groupId);

        userGroupRepository.saveAll(userGroups);

    }

    @Transactional
    public void addNewUserToGroup(Group group, List<String> usersId){
        String groupId = group.getGroupId();

        usersId.add(group.getOwnerId());

        List<UserGroup> userGroups = saveUserGroup(usersId, groupId);

        userGroupRepository.saveAll(userGroups);
    }

    private List<UserGroup> saveUserGroup(List<String> usersId, String groupId){
        List<UserGroup> userGroups = new ArrayList<>();
        usersId.forEach(s -> {
            UserGroup userGroup = new UserGroup();

            UserGroup.UserGroupId userGroupId = new UserGroup.UserGroupId();
            userGroupId.setUserId(s);
            userGroupId.setGroupId(groupId);

            userGroup.setId(userGroupId);
            userGroups.add(userGroup);
        });
        return userGroups;
    }

}
