package com.example.quiz_World.service;

import com.example.quiz_World.entities.Role;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.UserDTO;
import com.example.quiz_World.exceptions.ExistsException;
import com.example.quiz_World.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapEntity mapEntity;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MapEntity mapEntity) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapEntity = mapEntity;
    }

    public UserDTO createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ExistsException("User with this email is registered");
        }

        user.setRole(Role.USER);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User createUser = userRepository.save(user);
        return mapEntity.mapUserToUserDTO(createUser);
    }

    public List<UserDTO> findAllUsers() {

        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return mapEntity.mapUserListToUserDTOList(users);
    }
}

