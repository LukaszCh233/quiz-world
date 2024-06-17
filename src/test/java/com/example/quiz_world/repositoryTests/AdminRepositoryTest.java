package com.example.quiz_world.repositoryTests;

import com.example.quiz_world.entities.Admin;
import com.example.quiz_world.entities.Role;
import com.example.quiz_world.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
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
        assertEquals(adminList.get(0).getId(), admin.getId());
        assertEquals(adminList.get(0).getName(), admin.getName());
        assertEquals(adminList.get(0).getEmail(), admin.getEmail());
    }

    @Test
    public void shouldFindAdminByEmail_Test() {
        //Given
        Admin admin = new Admin(null, "testName", "testEmail", "testPassword", Role.ADMIN);
        adminRepository.save(admin);

        //When
        Optional<Admin> findAdmin = adminRepository.findByEmail(admin.getEmail());

        //Then
        assertTrue(findAdmin.isPresent());
        String foundEmail = findAdmin.get().getEmail();
        assertEquals(foundEmail, admin.getEmail());
    }
}
