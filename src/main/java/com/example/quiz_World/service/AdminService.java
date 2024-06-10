package com.example.quiz_World.service;

import com.example.quiz_World.dto.AdminDTO;
import com.example.quiz_World.entities.Admin;
import com.example.quiz_World.entities.Role;
import com.example.quiz_World.exception.ExistsException;
import com.example.quiz_World.mapper.MapEntity;
import com.example.quiz_World.repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapEntity mapEntity;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, MapEntity mapEntity) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapEntity = mapEntity;
    }

    public AdminDTO createAdmin(Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new ExistsException("Admin with this email is registered");
        }

        admin.setRole(Role.ADMIN);
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        Admin createAdmin = adminRepository.save(admin);
        return mapEntity.mapAdminToAdminDTO(createAdmin);
    }

    public List<AdminDTO> findAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        if (admins.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return mapEntity.mapAdminsToAdminsDTO(admins);
    }
}
