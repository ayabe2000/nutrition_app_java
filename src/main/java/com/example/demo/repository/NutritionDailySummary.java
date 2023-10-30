package com.example.demo.repository;

import java.time.LocalDate;

public interface NutritionDailySummary {
    LocalDate getDate();
    Double getGrams();
    String getFoodName();
    Double getEnergySum();
    Double getProteinSum();
    Double getFatSum();
    Double getCholesterolSum();
    Double getCarbohydratesSum();
}