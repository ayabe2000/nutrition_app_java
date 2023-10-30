package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Nutrition;
import com.example.demo.model.NutritionHistory;
import com.example.demo.repository.NutritionDailySummary;
import com.example.demo.service.NutritionService;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/nutrition")
public class NutritionController {

    @Autowired
    private NutritionService nutritionService;

    private static final Logger logger = LoggerFactory.getLogger(NutritionController.class);
    
    // 指定した食品名とグラム数に基づいて栄養を計算するエンドポイント
    @GetMapping("/calculate")
    public ResponseEntity<?> calculateNutrition(@RequestParam String foodName, @RequestParam double grams) {
        logger.info("Entering calculateNutrition method");

        logger.info("Received request to calculate nutrition for foodName: {} and grams: {}", foodName, grams);

        try {
            Nutrition nutrition = nutritionService.getNutritionByFoodName(foodName);
            if (nutrition == null) {
                logger.warn("Food not found: {}", foodName);
                return new ResponseEntity<>("指定された食品が見つかりませんでした。", HttpStatus.NOT_FOUND);
            }

            // 各フィールドを計算して設定
            nutrition.setEnergy(nutrition.getEnergy() * grams / 100);
            nutrition.setProtein(nutrition.getProtein() * grams / 100);
            nutrition.setFat(nutrition.getFat() * grams / 100);
            nutrition.setCholesterol(nutrition.getCholesterol() * grams / 100);
            nutrition.setCarbohydrates(nutrition.getCarbohydrates() * grams / 100);

            logger.info("Calculated nutrition for food: {} and grams: {}", foodName, grams);
            return ResponseEntity.ok(nutrition);
        } catch (Exception e) {
            logger.error("食品の栄養情報の取得中にエラーが発生しました：", e);
            return new ResponseEntity<>("内部エラーが発生しました。", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String dailySummaries(Model model) {
        List<NutritionDailySummary> summaries = nutritionService.getDailySummaries();
        model.addAttribute("summaries", summaries);
        return "daily-summaries";
    }

    // 今日の栄養情報をユーザーの履歴として保存するエンドポイント。
    @PostMapping("/add")
    public ResponseEntity<String> addNutritionForToday(@RequestBody Nutrition nutrition, @RequestParam String username) {
        LocalDate today = LocalDate.now();
        NutritionHistory nutritionHistory = convertToNutritionHistory(nutrition, username);
        nutritionService.addNutritionForDate(nutritionHistory, today, username);
        return ResponseEntity.ok("Nutrition added for today");
    }
     // NutritionオブジェクトをNutritionHistoryオブジェクトに変換するヘルパーメソッド
    private NutritionHistory convertToNutritionHistory(Nutrition nutrition, String username) {
        NutritionHistory nutritionHistory = new NutritionHistory();
        
        nutritionHistory.setUsername(username);
        nutritionHistory.setEnergy(nutrition.getEnergy());
        nutritionHistory.setProtein(nutrition.getProtein());
        nutritionHistory.setFat(nutrition.getFat());
        nutritionHistory.setCholesterol(nutrition.getCholesterol());
        nutritionHistory.setCarbohydrates(nutrition.getCarbohydrates());


        return nutritionHistory;
    }

}
