package com.example.backend.repositories;

import com.example.backend.model.Friends;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Integer> {

    Optional<Friends> findByUser(User userId);

}
