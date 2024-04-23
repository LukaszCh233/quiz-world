package com.example.quiz_World.repository;

import com.example.quiz_World.entities.CategoryWordSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryWordSetRepository extends JpaRepository<CategoryWordSet, Long> {

}
