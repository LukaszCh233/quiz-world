package com.example.quiz_world.user.repository;

import com.example.quiz_world.user.entity.Role;
import com.example.quiz_world.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);
}