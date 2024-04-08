package com.example.quiz_World.controller;

import ch.qos.logback.classic.Logger;
import com.example.quiz_World.config.HelpJwt;
import com.example.quiz_World.entities.Admin;
import com.example.quiz_World.entities.AdminDTO;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.UserDTO;
import com.example.quiz_World.exceptions.IncorrectPasswordException;
import com.example.quiz_World.repository.AdminRepository;
import com.example.quiz_World.repository.UserRepository;
import com.example.quiz_World.service.AdminServiceImpl;
import com.example.quiz_World.service.UserServiceImpl;
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
    private final AdminServiceImpl adminService;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final HelpJwt helpJwt;

    public AccessController(AdminServiceImpl adminService, AdminRepository adminRepository, UserRepository userRepository,
                            UserServiceImpl userService, PasswordEncoder passwordEncoder, HelpJwt helpJwt) {
        this.adminService = adminService;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.helpJwt = helpJwt;
    }

    @PostMapping("/userReg")
    ResponseEntity<?> registerCustomer(@RequestBody User user) {
        User createUser = userService.createUser(user);
        UserDTO userDTO = userService.mapUserToUserDTO(createUser);
        logger.info("User registered successfully: {}", createUser.getEmail());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/userLog")
    ResponseEntity<?> loginCustomer(@RequestBody User user) {
        User registeredUser = userRepository.findByEmail(user.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("Customer not exists"));

        if (!passwordEncoder.matches(user.getPassword(), registeredUser.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        String jwtToken = helpJwt.generateToken(registeredUser);
        logger.info("User logged in successfully: {}", registeredUser.getEmail());
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/adminReg")
    ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {

        Admin createAdmin = adminService.createAdmin(admin);
        AdminDTO adminDTO = adminService.mapAdminToAdminDTO(createAdmin);
        logger.info("User registered successfully: {}", createAdmin.getEmail());
        return new ResponseEntity<>(adminDTO, HttpStatus.OK);
    }

    @PostMapping("/adminLog")
    ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {

        Admin registeredAdmin = adminRepository.findByEmail(admin.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("Admin not exists"));

        if (!passwordEncoder.matches(admin.getPassword(), registeredAdmin.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        String jwtToken = helpJwt.generateToken(registeredAdmin);
        logger.info("User logged in successfully: {}", registeredAdmin.getEmail());
        return ResponseEntity.ok(jwtToken);
    }
}


