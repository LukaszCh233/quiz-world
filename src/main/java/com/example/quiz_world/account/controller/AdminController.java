package com.example.quiz_world.account.controller;

import com.example.quiz_world.account.user.AdminDTO;
import com.example.quiz_world.account.user.UserDTO;
import com.example.quiz_world.account.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> displayUsers() {
        List<UserDTO> userDTOList = userService.findAllUsers();

        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<AdminDTO>> displayAdmins() {
        List<AdminDTO> adminDTOList = userService.findAllAdmins();

        return ResponseEntity.ok(adminDTOList);
    }
}
