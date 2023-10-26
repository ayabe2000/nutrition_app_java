package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import com.example.demo.model.NutritionHistory;


public interface NutritionHistoryRepository extends JpaRepository<NutritionHistory, Long> {
    List<NutritionHistory> findByDate(LocalDate date);
    List<NutritionHistory> findAllByUsername(String username);
    List<NutritionHistory> findAllByFoodName(String foodName);
}