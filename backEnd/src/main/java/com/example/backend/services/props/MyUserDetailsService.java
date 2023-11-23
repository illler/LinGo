package com.example.backend.services.props;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.User;
import com.example.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final DTOService dtoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public User loadUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> findUserByNameOrEmail(String pattern, String userId){
        return userRepository.findAllByEmailOrName(pattern, pattern, userId);
    }
    @Transactional
    public void saveNewOrUpdateExistingUser(User user){
        userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<UserDTO> findAllUsersByTheirIds(List<String> listId){
        return listId.parallelStream()
                .map(this::loadUserById)
                .filter(Objects::nonNull)
                .map(dtoService::convertToUserDTO)
                .collect(Collectors.toList());
    }
}
