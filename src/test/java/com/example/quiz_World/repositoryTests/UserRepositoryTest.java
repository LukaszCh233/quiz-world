package com.example.quiz_World.repositoryTests;

import com.example.quiz_World.entities.Role;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.repository.ResultRepository;
import com.example.quiz_World.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("quizWorldTest")
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ResultRepository resultRepository;

    @BeforeEach
    public void setUp() {
        resultRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldSaveUserTest() {
        //Given
        User user = new User(null, "testName", "testEmail", "testPassword", Role.USER);

        //When
        userRepository.save(user);

        //Then
        List<User> userList = userRepository.findAll();

        assertFalse(userList.isEmpty());
        assertEquals(1, userList.size());
    }

    @Test
    public void shouldFindUserByEmail_Test() {
        //Given
        User user = new User(null, "testName", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        //When
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());

        //Then
        assertTrue(findUser.isPresent());
        String foundEmail = findUser.get().getEmail();
        assertEquals(foundEmail, user.getEmail());
        assertEquals(user.getRole(), findUser.get().getRole());
    }
}
