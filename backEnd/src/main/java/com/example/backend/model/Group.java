package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_id")
    private String groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "owner_id")
    private String ownerId;


}
