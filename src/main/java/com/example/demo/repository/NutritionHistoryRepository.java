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



  
    @Query("SELECT " +
       "MIN(n.id) AS id, " +
       "n.date AS date, " +
       "string_agg(n.foodName, ', ') AS foodNames, " + // foodNamesをカンマで区切って連結
       "SUM(n.grams) AS grams, " +
       "SUM(n.energy) AS energy, " +
       "SUM(n.protein) AS protein, " +
       "SUM(n.fat) AS fat, " +
       "SUM(n.cholesterol) AS cholesterol, " +
       "SUM(n.carbohydrates) AS carbohydrates " +
       "FROM NutritionHistory n " +
       "GROUP BY n.date " +
       "ORDER BY n.date DESC")
List<NutritionDailySummary> findDailySummaries();

}


