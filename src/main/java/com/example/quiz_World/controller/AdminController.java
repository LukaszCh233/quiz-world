package com.example.quiz_World.controller;

import ch.qos.logback.classic.Logger;
import com.example.quiz_World.entities.UserDTO;
import com.example.quiz_World.service.UserServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final Logger logger = (Logger) LoggerFactory.getLogger(AdminController.class);
    private final UserServiceImpl userService;


    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers() {
        List<UserDTO> users = userService.findAllUsers();
        logger.info("wszystko git");
        return ResponseEntity.ok(users);
    }
}
