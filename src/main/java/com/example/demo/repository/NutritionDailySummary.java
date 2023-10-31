package com.example.demo.repository;

import java.time.LocalDate;

public interface NutritionDailySummary {
    Long getId();
    LocalDate getDate();
    Double getGrams();
    String getFoodName();
    Double getEnergy();
    Double getProtein();
    Double getFat();
    Double getCholesterol();
    Double getCarbohydrates();
}
