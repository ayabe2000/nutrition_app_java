package com.example.demo.Repository;

import com.example.demo.model.NutritionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutritionRepository extends JpaRepository<NutritionEntity, Long> {
    @Query("SELECT n.foodName FROM NutritionEntity n")
    List<String> findAllFoodNames();
}
