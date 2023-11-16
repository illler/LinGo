package com.example.backend.conrollers;

import com.example.backend.DTO.GroupDTO;
import com.example.backend.model.Group;
import com.example.backend.repositories.UserGroupRepository;
import com.example.backend.services.props.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/getAllGroups/{userId}")
    public List<Group> getAllGroups(@PathVariable String userId){
        return groupService.getAllGroups(userId);
    }

    @PostMapping("/createNewGroup")
    public void createNewGroup(@RequestBody GroupDTO groupDTO){
        groupService.addNewGroup(groupDTO.getGroup(), groupDTO.getUsersId());
    }

    @PostMapping("/addUserToGroup")
    public void addUserToGroup(@RequestBody GroupDTO groupDTO){
        groupService.addNewUserToGroup(groupDTO.getGroup(), groupDTO.getUsersId());
    }

}
