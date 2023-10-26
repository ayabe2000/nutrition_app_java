package com.example.demo.repository;


import com.example.demo.model.NutritionHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



import java.time.LocalDate;
import java.util.List;

public interface NutritionHistoryRepository extends JpaRepository<NutritionHistory, Long> {
    List<NutritionHistory> findByUsername(String username);
    List<NutritionHistory> findByDate(LocalDate date);
    List<NutritionHistory> findAllByUsername(@Param("username") String username);


    @Query("SELECT n.date AS date, n.foodName AS foodName, SUM(n.grams) AS grams, SUM(n.energy) AS energySum, SUM(n.protein) AS proteinSum, SUM(n.fat) AS fatSum, SUM(n.cholesterol) AS cholesterolSum, SUM(n.carbohydrates) AS carbohydratesSum FROM NutritionHistory n GROUP BY n.date, n.foodName ORDER BY n.date DESC")
    List<NutritionDailySummary> findDailySummaries();
}




