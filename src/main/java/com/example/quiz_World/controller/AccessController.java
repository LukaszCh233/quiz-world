package com.example.quiz_World.controller;

import ch.qos.logback.classic.Logger;
import com.example.quiz_World.config.HelpJwt;
import com.example.quiz_World.dto.AdminDTO;
import com.example.quiz_World.dto.UserDTO;
import com.example.quiz_World.entities.Admin;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.exception.IncorrectPasswordException;
import com.example.quiz_World.repository.AdminRepository;
import com.example.quiz_World.repository.UserRepository;
import com.example.quiz_World.service.AdminService;
import com.example.quiz_World.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/access")

public class AccessController {
    private final Logger logger = (Logger) LoggerFactory.getLogger(AccessController.class);
    private final AdminService adminService;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final HelpJwt helpJwt;

    public AccessController(AdminService adminService, AdminRepository adminRepository, UserRepository userRepository,
                            UserService userService, PasswordEncoder passwordEncoder, HelpJwt helpJwt) {
        this.adminService = adminService;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.helpJwt = helpJwt;
    }

    @PostMapping("/userReg")
    ResponseEntity<UserDTO> registerCustomer(@RequestBody User user) {
        UserDTO createUserDTO = userService.createUser(user);

        logger.info("User registered successfully: {}", createUserDTO.email());
        return new ResponseEntity<>(createUserDTO, HttpStatus.OK);
    }

    @PostMapping("/userLog")
    ResponseEntity<?> loginCustomer(@RequestBody User user) {
        User registeredUser = userRepository.findByEmail(user.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("User not exists"));

        if (!passwordEncoder.matches(user.getPassword(), registeredUser.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        String jwtToken = helpJwt.generateToken(registeredUser);
        logger.info("User logged in successfully: {}", registeredUser.getEmail());
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/adminReg")
    ResponseEntity<AdminDTO> registerAdmin(@RequestBody Admin admin) {

        AdminDTO createAdmin = adminService.createAdmin(admin);

        logger.info("Admin registered successfully: {}", createAdmin.email());
        return new ResponseEntity<>(createAdmin, HttpStatus.OK);
    }

    @PostMapping("/adminLog")
    ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {

        Admin registeredAdmin = adminRepository.findByEmail(admin.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("Admin not exists"));

        if (!passwordEncoder.matches(admin.getPassword(), registeredAdmin.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        String jwtToken = helpJwt.generateToken(registeredAdmin);
        logger.info("Admin logged in successfully: {}", registeredAdmin.getEmail());
        return ResponseEntity.ok(jwtToken);
    }
}


