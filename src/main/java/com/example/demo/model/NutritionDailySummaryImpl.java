package com.example.demo.model;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import com.example.demo.repository.NutritionDailySummary;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Getter
@Setter
@Entity
public class NutritionDailySummaryImpl implements NutritionDailySummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private Double grams;
    private String foodName;
    private Double energy;
    private Double protein;
    private Double fat;
    private Double cholesterol;
    private Double carbohydrates;


}
