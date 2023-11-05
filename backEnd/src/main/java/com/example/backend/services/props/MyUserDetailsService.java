package com.example.backend.services.props;

import com.example.backend.model.User;
import com.example.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

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

    public List<User> findUserByNameOrEmail(String pattern){
        return userRepository.findAllByEmailOrName(pattern, pattern);
    }


    public void saveNewOrUpdateExistingUser(User user){
        userRepository.save(user);
    }
}
