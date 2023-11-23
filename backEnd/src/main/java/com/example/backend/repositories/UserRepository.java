package com.example.backend.repositories;

import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE (u.email LIKE CONCAT(:email, '%') " +
            "OR u.firstname LIKE CONCAT(:name, '%') " +
            "OR u.lastname LIKE CONCAT(:name, '%')) " +
            "and u.id <>:id ")
    List<User> findAllByEmailOrName(String email, String name, String id);

}
