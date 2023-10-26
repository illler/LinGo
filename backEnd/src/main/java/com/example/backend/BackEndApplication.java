package com.example.backend;


import com.example.backend.auth.RegisterRequest;
import com.example.backend.services.AuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.backend.model.Role.*;

@SpringBootApplication
public class BackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService service){
        return args -> {
            var admin = RegisterRequest.builder()
                    .firstname("Admin")
                    .lastname("Admin")
                    .email("admin@gmail.ru")
                    .password("password")
                    .role(ADMIN)
                    .build();
            System.out.println("Admin token: " + service.register(admin).getToken());

            var manager = RegisterRequest.builder()
                    .firstname("Manager")
                    .lastname("Manager")
                    .email("manager@gmail.ru")
                    .password("password")
                    .role(MANAGER)
                    .build();
            System.out.println("Manager token: " + service.register(manager).getToken());

            var user = RegisterRequest.builder()
                    .firstname("user1")
                    .lastname("user1")
                    .email("user1@gmail.ru")
                    .password("password")
                    .role(USER)
                    .build();
            System.out.println("User token: " + service.register(user).getToken());
            var user2 = RegisterRequest.builder()
                    .firstname("user2")
                    .lastname("user2")
                    .email("user2@gmail.ru")
                    .password("password")
                    .role(USER)
                    .build();
            System.out.println("User2 token: " + service.register(user2).getToken());
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
