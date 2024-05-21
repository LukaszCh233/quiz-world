package com.example.quiz_World.service;

import com.example.quiz_World.entities.Admin;
import com.example.quiz_World.entities.AdminDTO;
import com.example.quiz_World.entities.Role;
import com.example.quiz_World.exceptions.ExistsException;
import com.example.quiz_World.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapEntity mapEntity;

    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder, MapEntity mapEntity) {
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
}
