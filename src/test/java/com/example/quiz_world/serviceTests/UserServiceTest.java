package com.example.quiz_world.serviceTests;

import com.example.quiz_world.dto.UserDTO;
import com.example.quiz_world.entities.Role;
import com.example.quiz_world.entities.User;
import com.example.quiz_world.exception.ExistsException;
import com.example.quiz_world.repository.UserRepository;
import com.example.quiz_world.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserServiceTest(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;

    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser_Test() {
        //Given
        User user = new User(null, "user", "email", "password", Role.USER);

        //When
        UserDTO createUser = userService.createUser(user);

        //Then
        assertNotNull(createUser);
        assertEquals(user.getId(), createUser.id());
        assertEquals(user.getEmail(), createUser.email());
        assertEquals(user.getName(), createUser.name());
    }

    @Test
    void shouldCreateUser_ExistingEmail() {
        //Given
        User user = new User(null, "user", "email", "password", Role.USER);
        ///when

        userRepository.save(user);
        //then
        assertThrows(ExistsException.class, () -> userService.createUser(user));
    }
}
