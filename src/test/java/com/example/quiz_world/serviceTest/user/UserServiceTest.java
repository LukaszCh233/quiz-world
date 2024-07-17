package com.example.quiz_world.serviceTest.user;

import com.example.quiz_world.user.dto.AdminDTO;
import com.example.quiz_world.user.dto.UserDTO;
import com.example.quiz_world.user.entity.LoginRequest;
import com.example.quiz_world.user.entity.Role;
import com.example.quiz_world.user.entity.User;
import com.example.quiz_world.user.repository.UserRepository;
import com.example.quiz_world.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void createdUserShouldBeFindInUsersList_test() {
        newUser("user@example.com", "password", Role.USER);

        List<UserDTO> usersList = userService.findAllUsers();

        Assertions.assertEquals(usersList.size(), 1);
        Assertions.assertEquals(usersList.get(0).email(), "user@example.com");
    }

    @Test
    public void createdAdminShouldBeFindInAdminsList_test() {
        newUser("admin@example.com", "password", Role.ADMIN);

        List<AdminDTO> usersList = userService.findAllAdmins();

        Assertions.assertEquals(usersList.size(), 1);
        Assertions.assertEquals(usersList.get(0).email(), "admin@example.com");
    }

    @Test
    public void whenUserAuthorizationIsCorrectShouldGenerateToken_test() {
        newUser("user@example.com", "userPassword", Role.USER);
        LoginRequest loginRequest = new LoginRequest("user@example.com", "userPassword");

        String token = userService.userAuthorization(loginRequest);

        assertNotNull(token);
    }
    @Test
    public void whenAdminAuthorizationIsCorrectShouldGenerateToken_test() {
        newUser("admin@example.com", "adminPassword", Role.ADMIN);
        LoginRequest loginRequest = new LoginRequest("admin@example.com", "adminPassword");

        String token = userService.adminAuthorization(loginRequest);

        assertNotNull(token);
    }

    private void newUser(String email, String password, Role role) {
        User user = new User();
        user.setName("name");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);
    }

    private void newAdmin(String email, Role role) {
        User admin = new User();
        admin.setName("name");
        admin.setEmail(email);
        admin.setPassword("password");
        admin.setRole(role);

        userRepository.save(admin);
    }
}