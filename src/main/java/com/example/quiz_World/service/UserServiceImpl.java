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

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {

        userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new ExistsException("User exists");
        });

        user.setRole(Role.USER);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public List<UserDTO> findAllUsers() {

        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return
                users.stream()
                        .map(this::mapUserToUserDTO)
                        .toList();
    }

    public UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}

