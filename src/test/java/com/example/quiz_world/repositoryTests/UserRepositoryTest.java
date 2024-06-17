package com.example.quiz_world.repositoryTests;

import com.example.quiz_world.entities.Role;
import com.example.quiz_world.entities.User;
import com.example.quiz_world.repository.ResultRepository;
import com.example.quiz_world.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
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
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Test
    public void testDatasourceUrl() {
        assertEquals("jdbc:h2:mem:testdb", datasourceUrl);
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
        assertEquals(userList.get(0).getId(), user.getId());
        assertEquals(userList.get(0).getName(), user.getName());
        assertEquals(userList.get(0).getEmail(), user.getEmail());
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