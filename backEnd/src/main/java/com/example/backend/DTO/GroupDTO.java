package com.example.backend.DTO;

import com.example.backend.model.Group;
import lombok.Data;

import java.util.List;

@Data
public class GroupDTO {

    private Group group;

    private List<String> usersId;


}
