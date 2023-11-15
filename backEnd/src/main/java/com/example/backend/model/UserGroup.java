package com.example.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "user_groups")
@Data
@NoArgsConstructor
public class UserGroup {

    @EmbeddedId
    private UserGroupId id;


    public UserGroup(String userId, String groupId) {
        this.id = new UserGroupId(userId, groupId);
    }


    @Embeddable
    @Data
    @AllArgsConstructor
    public static class UserGroupId implements Serializable {
        @Column(name = "user_id")
        private String userId;

        @Column(name = "group_id")
        private String groupId;

        public UserGroupId() {

        }
    }
}

