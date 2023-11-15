package com.example.backend.repositories;

import com.example.backend.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroup.UserGroupId> {

    @Query("SELECT ug.id.groupId FROM UserGroup ug WHERE ug.id.userId = :userId")
    List<String> findGroupIdsByUserId(@Param("userId") String userId);
}
