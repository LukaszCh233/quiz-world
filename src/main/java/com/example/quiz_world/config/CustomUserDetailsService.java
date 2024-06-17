package com.example.quiz_world.config;

import com.example.quiz_world.entities.Admin;
import com.example.quiz_world.entities.User;
import com.example.quiz_world.repository.AdminRepository;
import com.example.quiz_world.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if (adminOptional.isPresent()) {
            return adminOptional.get();
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}