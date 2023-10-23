package com.example.demo.repository;

import com.example.demo.model.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    Nutrition findByFoodName(String foodName);

}

