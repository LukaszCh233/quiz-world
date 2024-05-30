package com.example.quiz_World.serviceTests;

import com.example.quiz_World.entities.Role;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.UserDTO;
import com.example.quiz_World.exceptions.ExistsException;
import com.example.quiz_World.repository.ResultRepository;
import com.example.quiz_World.repository.UserRepository;
import com.example.quiz_World.service.UserServiceImpl;
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
    private final UserServiceImpl userService;
    private final ResultRepository resultRepository;

    @Autowired
    public UserServiceTest(UserRepository userRepository, UserServiceImpl userService, ResultRepository resultRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.resultRepository = resultRepository;
    }

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();
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
        assertEquals(user.getId(), createUser.getId());
        assertEquals(user.getEmail(), createUser.getEmail());
        assertEquals(user.getName(), createUser.getName());
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
