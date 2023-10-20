package com.example.backend.repository;

import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);


    @Test
    public void UserRepository_SaveAll_ReturnSavedUser(){

        User user = User.builder()
                .email("test@gmail.com")
                .firstname("Test")
                .lastname("Test")
                .password("test")
                .tokens(null)
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getEmail()).contains("@");

        logger.info("Saved user: {}", savedUser);

    }

    @Test
    public void UserRepository_GetAll_ReturnMoreThenOneUser(){
        User user = User.builder()
                .email("test@gmail.com")
                .firstname("Test")
                .lastname("Test")
                .password("test")
                .tokens(null)
                .role(Role.USER)
                .build();

        User user2 = User.builder()
                .email("test@gmail.com")
                .firstname("Test")
                .lastname("Test")
                .password("test")
                .tokens(null)
                .role(Role.USER)
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isGreaterThan(2);

        logger.info("All users: {}", users);
    }


    @Test
    public void UserRepository_FindByEmail_ReturnOneUserNotNull(){
        User user = User.builder()
                .email("test@gmail.com")
                .firstname("Test")
                .lastname("Test")
                .password("test")
                .tokens(null)
                .role(Role.USER)
                .build();

        userRepository.save(user);

        User userFind = userRepository.findByEmail("test@gmail.com").orElse(null);

        Assertions.assertThat(userFind).isNotNull();
        logger.info("Found user: {}", userFind);

    }

}
