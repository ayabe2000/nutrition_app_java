package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Nutrition;
import com.example.demo.service.NutritionService;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/nutrition")
public class NutritionController {

    @Autowired
    private NutritionService nutritionService;

    @GetMapping("/calculate")
    public Nutrition calculateNutrition(@RequestParam String foodName, @RequestParam double grams) {
        Nutrition nutrition = nutritionService.getNutritionByFoodName(foodName);
        return nutritionService.calculateNutritionForGrams(nutrition, grams);
    }
}