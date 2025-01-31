package com.example.quiz_world.account.controller;

import com.example.quiz_world.account.user.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/access")
public class AccessController {
    private final UserService userService;

    public AccessController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user-register")
    ResponseEntity<UserDTO> registerUser(@Valid @RequestBody User user) {
        UserDTO createUserDTO = userService.createUser(user);

        return ResponseEntity.ok(createUserDTO);
    }

    @PostMapping("/user-login")
    ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest user) {
        String jwtToken = userService.userAuthorization(user);

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/admin-register")
    ResponseEntity<AdminDTO> registerAdmin(@Valid @RequestBody User admin) {

        AdminDTO createAdmin = userService.createAdmin(admin);

        return ResponseEntity.ok(createAdmin);
    }

    @PostMapping("/admin-login")
    ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequest admin) {
        String jwtToken = userService.adminAuthorization(admin);

        return ResponseEntity.ok(jwtToken);
    }
}




