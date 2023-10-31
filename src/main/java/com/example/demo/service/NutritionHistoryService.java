package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.repository.NutritionHistoryRepository;
import com.example.demo.model.Nutrition;
import com.example.demo.model.NutritionHistory;
import java.util.List;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





@Service
public class NutritionHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionHistoryService.class);


    @Autowired
    private NutritionHistoryRepository nutritionHistoryRepository;

    //過去の栄養履歴を取得
    public List<NutritionHistory> getPastNutritionHistoryForUser(String username) {
        logger.debug("Fetching past nutrition history for user: {}", username);
        return nutritionHistoryRepository.findAllByUsername(username);
    }

    //栄養履歴に新しいエントリを追加
    public NutritionHistory addNutritionHistoryForUser(Nutrition nutrition, String username) {
        logger.debug("Adding nutrition history for user: {}", username);
        NutritionHistory nutritionHistory = convertToNutritionHistory(nutrition);
        nutritionHistory.setUsername(username);
        return nutritionHistoryRepository.save(nutritionHistory);
    }
    

     // NutritionオブジェクトをNutritionHistoryオブジェクトに変換するプライベートメソッド
    private NutritionHistory convertToNutritionHistory(Nutrition nutrition) {
        logger.debug("Converting Nutrition object to NutritionHistory object");
        NutritionHistory nutritionHistory = new NutritionHistory();

        nutritionHistory.setId(nutrition.getId());
        nutritionHistory.setFoodName(nutrition.getFoodName());
        nutritionHistory.setEnergy(nutrition.getEnergy());
        nutritionHistory.setProtein(nutrition.getProtein());
        nutritionHistory.setFat(nutrition.getFat());
        nutritionHistory.setCholesterol(nutrition.getCholesterol());
        nutritionHistory.setCarbohydrates(nutrition.getCarbohydrates());
        return nutritionHistory;
    }

    
}
