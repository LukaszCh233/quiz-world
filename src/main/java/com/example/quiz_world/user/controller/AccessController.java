package com.example.quiz_world.user.controller;

import com.example.quiz_world.config.HelpJwt;
import com.example.quiz_world.exception.IncorrectPasswordException;
import com.example.quiz_world.user.dto.AdminDTO;
import com.example.quiz_world.user.dto.UserDTO;
import com.example.quiz_world.user.entity.User;
import com.example.quiz_world.user.repository.UserRepository;
import com.example.quiz_world.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class AccessController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final HelpJwt helpJwt;

    public AccessController(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder,
                            HelpJwt helpJwt) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.helpJwt = helpJwt;
    }

    @PostMapping("/user-register")
    ResponseEntity<UserDTO> registerCustomer(@Valid @RequestBody User user) {
        UserDTO createUserDTO = userService.createUser(user);

        return ResponseEntity.ok(createUserDTO);
    }

    @PostMapping("/user-login")
    ResponseEntity<?> loginCustomer(@Valid @RequestBody User user) {
        User registeredUser = userRepository.findByEmail(user.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("User not exists"));

        if (!passwordEncoder.matches(user.getPassword(), registeredUser.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        String jwtToken = helpJwt.generateToken(registeredUser);

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/admin-register")
    ResponseEntity<AdminDTO> registerAdmin(@Valid @RequestBody User admin) {

        AdminDTO createAdmin = userService.createAdmin(admin);

        return ResponseEntity.ok(createAdmin);
    }

    @PostMapping("/admin-login")
    ResponseEntity<?> loginAdmin(@Valid @RequestBody User admin) {

        User registeredAdmin = userRepository.findByEmail(admin.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("Admin not exists"));

        if (!passwordEncoder.matches(admin.getPassword(), registeredAdmin.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        String jwtToken = helpJwt.generateToken(registeredAdmin);

        return ResponseEntity.ok(jwtToken);
    }
}




