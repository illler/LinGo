package com.example.backend.services.props;

import com.example.backend.DTO.UserDTO;
import com.example.backend.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DTOService {

    private final ModelMapper modelMapper;

    public UserDTO convertToPersonDTO(User person) {
        return modelMapper.map(person, UserDTO.class);
    }

    public User convertToPerson(UserDTO personDTO) {
        return modelMapper.map(personDTO, User.class);
    }
}
