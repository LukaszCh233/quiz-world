package com.example.quiz_world.account.user;

import com.example.quiz_world.config.HelpJwt;
import com.example.quiz_world.exception.ExistsException;
import com.example.quiz_world.exception.IncorrectPasswordException;
import com.example.quiz_world.mapper.MapperEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapperEntity mapperEntity;
    private final HelpJwt helpJwt;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MapperEntity mapperEntity,
                       HelpJwt helpJwt) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapperEntity = mapperEntity;
        this.helpJwt = helpJwt;
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

    public String userAuthorization(LoginRequest loginRequest) {
        User registeredUser = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("User not exists"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), registeredUser.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        return helpJwt.generateToken(registeredUser);
    }

    public String adminAuthorization(LoginRequest loginRequest) {
        User registeredAdmin = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("Admin not exists"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), registeredAdmin.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        return helpJwt.generateToken(registeredAdmin);
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

