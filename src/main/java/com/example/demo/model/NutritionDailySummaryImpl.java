package com.example.demo.model;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.example.demo.repository.NutritionDailySummary;


@Getter
@Setter
public class NutritionDailySummaryImpl implements NutritionDailySummary {
    private LocalDate date;
    private Double grams;
    private String foodName;
    private Double energySum;
    private Double proteinSum;
    private Double fatSum;
    private Double cholesterolSum;
    private Double carbohydratesSum;


}
