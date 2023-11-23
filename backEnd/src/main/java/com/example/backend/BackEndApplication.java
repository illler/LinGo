package com.example.backend;


import com.example.backend.auth.RegisterRequest;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.props.AuthenticationService;
import com.example.backend.services.props.FriendsService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.backend.model.Role.*;

@SpringBootApplication
public class BackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(AuthenticationService service){
//        return args -> {
//            var admin = RegisterRequest.builder()
//                    .firstname("Alexandr")
//                    .lastname("Pyatunin")
//                    .email("tezerakt10@gmail.com")
//                    .password("1234")
//                    .role(ADMIN)
//                    .build();
//            System.out.println("Admin token: " + service.register(admin).getToken());
//
//            var manager = RegisterRequest.builder()
//                    .firstname("Soboleva")
//                    .lastname("Elina")
//                    .email("tezerakt20@gmail.com")
//                    .password("1234")
//                    .role(MANAGER)
//                    .build();
//            System.out.println("Manager token: " + service.register(manager).getToken());
//
//            var user = RegisterRequest.builder()
//                    .firstname("Andrey")
//                    .lastname("Zharov")
//                    .email("tezerakt30@gmail.com")
//                    .password("1234")
//                    .role(USER)
//                    .build();
//            System.out.println("User token: " + service.register(user).getToken());
//
//        };
//    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
