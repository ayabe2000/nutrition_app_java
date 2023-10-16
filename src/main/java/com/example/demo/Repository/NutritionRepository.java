package com.example.demo.Repository;

import com.example.demo.model.NutritionEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NutritionRepository extends JpaRepository<NutritionEntity, Long> {
}

