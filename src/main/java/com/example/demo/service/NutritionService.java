package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.NutritionRepository;
import com.example.demo.model.Nutrition;


@Service
public class NutritionService {

    @Autowired
    private NutritionRepository nutritionRepository;

    public Nutrition getNutritionByFoodName(String foodName) {
        return nutritionRepository.findByFoodName(foodName);
    }

    public Nutrition calculateNutritionForGrams(Nutrition nutrition, double grams) {
        nutrition.setEnergy(nutrition.getEnergy() * grams / 100);
        nutrition.setProtein(nutrition.getProtein() * grams / 100);
        nutrition.setFat(nutrition.getFat() * grams / 100);
        nutrition.setCholesterol(nutrition.getCholesterol() * grams / 100);
        nutrition.setCarbohydrates(nutrition.getCarbohydrates() * grams / 100);
        return nutrition;
    }
}
