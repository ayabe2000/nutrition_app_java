package com.example.demo.repository;


import com.example.demo.model.NutritionHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NutritionHistoryRepository extends JpaRepository<NutritionHistory, Long> {
    List<NutritionHistory> findByUsername(String username);
    List<NutritionHistory> findByDate(LocalDate date);
    List<NutritionHistory> findAllByUsername(String username);
    
}

