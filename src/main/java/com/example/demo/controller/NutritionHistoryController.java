package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Nutrition;
import com.example.demo.model.NutritionHistory;
import com.example.demo.service.NutritionService;
import com.example.demo.service.NutritionHistoryService;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;


@RestController
@RequestMapping("/nutrition/history")
public class NutritionHistoryController {

    @Autowired
    private NutritionHistoryService nutritionHistoryService;

    @Autowired
    private NutritionService nutritionService;
    
    // foodNameとgramsのパラメータを受け取り、食品の栄養を計算して返すエンドポイント
    @GetMapping("/calculate")
    public ResponseEntity<?> calculateNutrition(@RequestParam String foodName, @RequestParam double grams) {
        try {

            // 指定された食品名に基づいて栄養情報を取得
            Nutrition nutrition = nutritionService.getNutritionByFoodName(foodName);
            if (nutrition == null) {
                return new ResponseEntity<>("指定された食品が見つかりませんでした。", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(nutritionService.calculateNutritionForGrams(nutrition, grams));
        } catch (Exception e) {
            // 例外をキャッチしてエラーレスポンスを返すか、適切な処理を行う
            return new ResponseEntity<>("エラーが発生しました。", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   
    // ユーザーの栄養履歴を取得するエンドポイント
    @GetMapping("/user")
    public ResponseEntity<?> getNutritionHistoryForUser(@RequestParam String username) {
        try {

            // 指定されたユーザー名に基づいて過去の栄養履歴を取得
            List<NutritionHistory> historyList = nutritionHistoryService.getPastNutritionHistoryForUser(username);
            return ResponseEntity.ok(historyList);
        } catch (Exception e) {
            // 例外をキャッチしてエラーレスポンスを返すか、適切な処理を行う
            return new ResponseEntity<>("ユーザーの栄養履歴の取得中にエラーが発生しました。", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ユーザーの栄養履歴を追加するエンドポイント
    @PostMapping("/add")
    public ResponseEntity<String> addNutritionHistoryForUser(@RequestBody Nutrition nutrition, @RequestParam String username) {
        nutritionHistoryService.addNutritionHistoryForUser(nutrition, username);
        return ResponseEntity.ok("Nutrition history added for user");
    }
}