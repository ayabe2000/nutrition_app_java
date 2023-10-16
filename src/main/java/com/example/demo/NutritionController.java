package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Repository.NutritionRepository;
import com.example.demo.model.NutritionEntity;

import java.util.List;


@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    @Autowired
    private NutritionRepository nutritionRepository;

    @PostMapping
    public NutritionEntity createNutrition(@RequestBody NutritionEntity nutritionEntity) {
        return nutritionRepository.save(nutritionEntity);
    }

    @GetMapping
    public List<NutritionEntity> getAllNutrition() {
        return nutritionRepository.findAll();
    }
}



