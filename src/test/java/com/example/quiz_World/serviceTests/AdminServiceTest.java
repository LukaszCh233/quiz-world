package com.example.quiz_World.serviceTests;

import com.example.quiz_World.entities.Admin;
import com.example.quiz_World.dto.AdminDTO;
import com.example.quiz_World.entities.Role;
import com.example.quiz_World.exception.ExistsException;
import com.example.quiz_World.repository.AdminRepository;
import com.example.quiz_World.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AdminServiceTest {
    private final AdminRepository adminRepository;
    private final AdminService adminService;

    @Autowired
    public AdminServiceTest(AdminRepository adminRepository, AdminService adminService) {
        this.adminRepository = adminRepository;
        this.adminService = adminService;
    }

    @BeforeEach
    void setUp() {
        adminRepository.deleteAll();
    }

    @Test
    void shouldCreateAdmin_Test() {
        //Given
        Admin admin = new Admin(null, "admin", "email", "password", Role.ADMIN);

        //When
        AdminDTO createAdmin = adminService.createAdmin(admin);

        //Then
        assertNotNull(createAdmin);
        assertEquals(admin.getId(), createAdmin.id());
        assertEquals(admin.getEmail(), createAdmin.email());
        assertEquals(admin.getName(), createAdmin.name());
    }

    @Test
    void shouldCreateAdmin_ExistingEmail() {
        //Given
        Admin admin = new Admin(null, "testName", "testEmail", "testPassword", Role.ADMIN);

        //When
        adminService.createAdmin(admin);

        //Then
        assertThrows(ExistsException.class, () -> adminService.createAdmin(admin));
    }
}

