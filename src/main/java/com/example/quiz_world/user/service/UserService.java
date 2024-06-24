package com.example.quiz_world.user.service;

import com.example.quiz_world.exception.ExistsException;
import com.example.quiz_world.mapper.MapperEntity;
import com.example.quiz_world.user.dto.AdminDTO;
import com.example.quiz_world.user.dto.UserDTO;
import com.example.quiz_world.user.entity.Role;
import com.example.quiz_world.user.entity.User;
import com.example.quiz_world.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapperEntity mapperEntity;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MapperEntity mapperEntity) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapperEntity = mapperEntity;
    }

    public UserDTO createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ExistsException("User with this email is registered");
        }

        user.setRole(Role.USER);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return mapperEntity.mapUserToUserDTO(userRepository.save(user));
    }

    public AdminDTO createAdmin(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ExistsException("Admin with this email is registered");
        }
        user.setRole(Role.ADMIN);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return mapperEntity.mapAdminToAdminDTO(userRepository.save(user));
    }

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findByRole(Role.USER);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return mapperEntity.mapUserListToUserDTOList(users);
    }

    public List<AdminDTO> findAllAdmins() {
        List<User> admins = userRepository.findByRole(Role.ADMIN);
        if (admins.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return mapperEntity.mapAdminsToAdminsDTO(admins);
    }
}

