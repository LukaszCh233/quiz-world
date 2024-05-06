package com.example.quiz_World.repositoryTests;

import com.example.quiz_World.entities.Admin;
import com.example.quiz_World.entities.Role;
import com.example.quiz_World.repository.AdminRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("quizWorldTest")
public class AdminRepositoryTest {
    @Autowired
    AdminRepository adminRepository;

    @BeforeEach
    public void setUp() {
        adminRepository.deleteAll();
    }

    @Test
    public void shouldSaveAdmin_Test() {
        //Given
        Admin admin = new Admin(null, "testName", "testEmail", "testPassword", Role.ADMIN);

        //When
        adminRepository.save(admin);

        //Then
        List<Admin> adminList = adminRepository.findAll();

        assertFalse(adminList.isEmpty());
        assertEquals(1, adminList.size());

    }

    @Test
    public void shouldFindAdminByEmail_Test() {
        //Given
        Admin admin = new Admin(null, "testName", "testEmail", "testPassword", Role.ADMIN);
        adminRepository.save(admin);

        //When
        Optional<Admin> findAdmin = adminRepository.findByEmail(admin.getEmail());

        //Then
        Assertions.assertTrue(findAdmin.isPresent());
        String foundEmail = findAdmin.get().getEmail();
        Assertions.assertEquals(foundEmail, admin.getEmail());
    }
}
