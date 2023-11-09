package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class Friends {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "friend_list", columnDefinition = "TEXT[]")
    private Set<String> friends;

}

