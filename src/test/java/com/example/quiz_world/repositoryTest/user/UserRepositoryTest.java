package com.example.quiz_world.repositoryTest.user;

import com.example.quiz_world.account.user.Role;
import com.example.quiz_world.account.user.User;
import com.example.quiz_world.account.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void findUserByEmail_test() {
        newUser("test@example.com", Role.USER);
        newUser("test1@example.com", Role.USER);

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        Assertions.assertEquals(foundUser.get().getEmail(), "test@example.com");
    }

    @Test
    public void findUserByRole() {
        newUser("test@example.com", Role.USER);
        newUser("test1@example.com", Role.ADMIN);

        List<User> foundUser = userRepository.findByRole(Role.USER);

        Assertions.assertEquals(foundUser.get(0).getEmail(), "test@example.com");
    }

    private void newUser(String email, Role role) {
        User user = new User(null, "name", email, "password", role);
        userRepository.save(user);
    }
}
