package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Map;

import com.example.demo.repository.NutritionDailySummary;
import com.example.demo.repository.NutritionHistoryRepository;
import com.example.demo.repository.NutritionRepository;
import com.example.demo.model.Nutrition;
import com.example.demo.model.NutritionDailySummaryImpl;
import com.example.demo.model.NutritionHistory;



@Service
public class NutritionService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionService.class);


    @Autowired
    private NutritionHistoryRepository nutritionHistoryRepository;
   
    @Autowired
    private NutritionRepository nutritionRepository;

   

    public List<NutritionDailySummary> getDailySummaries() {
        return nutritionHistoryRepository.findDailySummaries();
    }


   
    //nutrition テーブルから食品名に基づいて栄養情報を取得
    public List<Nutrition> getAllFoods() {
        List<Nutrition> allFoods = nutritionRepository.findAll(); 
    
        // 取得される食品のリストが空の場合にログ表示
        if (allFoods == null || allFoods.isEmpty()) {
            logger.warn("No nutrition information found in the nutrition table.");
        } else {
            logger.info("Fetched {} food items from the nutrition table.", allFoods.size());
        }
    
        return allFoods;
    }


    //指定されたユーザー名の栄養履歴をデータベースから取得
    public List<NutritionHistory> getPastNutritionHistoryForUser(String username) {
        logger.info("Fetching nutrition history for user: {}", username);
        List<NutritionHistory> history = nutritionHistoryRepository.findAllByUsername(username);
        logger.info("Retrieved {} nutrition records for user: {}", history.size(), username);
        return history;
    }


    public List<NutritionDailySummary> getPastNutritionDailySummaryForUser(String username) {
        // まず、ユーザーの全ての栄養履歴を取得します
        List<NutritionHistory> histories = getPastNutritionHistoryForUser(username);
    
        // 日付ごとに履歴をグループ化します
        Map<LocalDate, List<NutritionHistory>> groupedByDate = histories.stream()
            .collect(Collectors.groupingBy(NutritionHistory::getDate));

        List<NutritionDailySummary> dailySummaries = new ArrayList<>();
        for (Map.Entry<LocalDate, List<NutritionHistory>> entry : groupedByDate.entrySet()) {
            NutritionDailySummaryImpl dailySummary = new NutritionDailySummaryImpl();
            dailySummary.setDate(entry.getKey());

            double totalEnergy = 0.0;
            double totalProtein = 0.0;
            double totalFat = 0.0;
            double totalCholesterol = 0.0;
            double totalCarbohydrates = 0.0;
            for (NutritionHistory history : entry.getValue()) {
                totalEnergy += history.getEnergy();
                totalProtein += history.getProtein();
                totalFat += history.getFat();
                totalCholesterol += history.getCholesterol();
                totalCarbohydrates += history.getCarbohydrates();
            }

            dailySummary.setEnergySum(totalEnergy);
            dailySummary.setProteinSum(totalProtein);
            dailySummary.setFatSum(totalFat);
            dailySummary.setCholesterolSum(totalCholesterol);
            dailySummary.setCarbohydratesSum(totalCarbohydrates);
            
            dailySummaries.add(dailySummary);
        }
        return dailySummaries;
    }
  




    //食品名に基づいて栄養情報を取得
    public Nutrition getNutritionByFoodName(String foodName) throws Exception {
        logger.info("Starting getNutritionByFoodName with foodName: {}", foodName);

        List<Nutrition> nutritionList = nutritionRepository.findAllByFoodName(foodName);

        if (nutritionList.size() > 1) {
            logger.error("Multiple entries found for food: {}", foodName);
            throw new Exception("Multiple entries found for food: " + foodName);
        }
        Nutrition nutrition = nutritionList.isEmpty() ? null : nutritionList.get(0);

        if (nutrition != null) {
            logger.info("Retrieved nutrition for food: {}", foodName);
        } else {
            logger.warn("No nutrition found for food: {}", foodName);
        }

        logger.info("Finished getNutritionByFoodName with foodName: {}", foodName);

        return nutrition;
    }
    
    
    //指定した日付、ユーザー名に関連づけられた栄養情報をデータベースに保存
    public NutritionHistory addNutritionForDate(NutritionHistory nutritionHistory, LocalDate date, String username) {
        try {
            if (nutritionHistory == null) {
                logger.error("NutritionHistory object is null.");
                return null;
            }
            logger.info("Adding nutrition for user: {} on date: {}", username, date);
            nutritionHistory.setDate(date);
            nutritionHistory.setUsername(username);
            logger.info("Successfully saved nutrition for user: {} on date: {}", username, date);
            return nutritionHistoryRepository.save(nutritionHistory);
        } catch (Exception e) {
            logger.error("Error occurred while saving nutrition data for user: {} on date: {}", username, date, e);
            return null;
        }
    }
    
    //baseNutritionとグラム数を元に計算してNutritionHistory オブジェクトに設定して返す。
    public NutritionHistory calculateNutritionForGrams(Nutrition baseNutrition, double grams) {
        logger.info("Entering calculateNutritionForGrams method");
        logger.info("Calculating nutrition for grams: {}", grams);
        if (baseNutrition == null){
            throw new IllegalArgumentException("Base nutrition cannot be null.");
        }

        // 既存の栄養価計算
        NutritionHistory calculatedNutrition = new NutritionHistory();
        calculatedNutrition.setEnergy(baseNutrition.getEnergy() * grams / 100);
        calculatedNutrition.setProtein(baseNutrition.getProtein() * grams / 100);
        calculatedNutrition.setFat(baseNutrition.getFat() * grams / 100);
        calculatedNutrition.setCholesterol(baseNutrition.getCholesterol() * grams / 100);
        calculatedNutrition.setCarbohydrates(baseNutrition.getCarbohydrates() * grams / 100);

        calculatedNutrition.setFoodName(baseNutrition.getFoodName());
        calculatedNutrition.setGrams(grams);
        logger.info("Calculation completed.");
        return calculatedNutrition;
    }  

    //calculateNutritionForGramsで計算された栄養情報を保存
    public NutritionHistory saveCalculatedNutritionForGrams(Nutrition baseNutrition, double grams, LocalDate date, String username) {
        NutritionHistory calculatedNutrition = calculateNutritionForGrams(baseNutrition, grams);
    
        NutritionHistory nutritionHistory = new NutritionHistory();
        nutritionHistory.setFoodName(calculatedNutrition.getFoodName());
        nutritionHistory.setGrams(grams);
        nutritionHistory.setDate(date);
        nutritionHistory.setUsername(username);
        
        // 計算された栄養素を設定
        nutritionHistory.setEnergy(calculatedNutrition.getEnergy());
        nutritionHistory.setProtein(calculatedNutrition.getProtein());
        nutritionHistory.setFat(calculatedNutrition.getFat());
        nutritionHistory.setCholesterol(calculatedNutrition.getCholesterol());
        nutritionHistory.setCarbohydrates(calculatedNutrition.getCarbohydrates());
    
        return nutritionHistoryRepository.save(nutritionHistory);
    }
    
    //現在の日付に関連付けられた栄養情報の合計を計算し、合計栄養情報を返す
    public Nutrition getTodayTotalNutrition() {
        logger.info("Fetching total nutrition for today.");
        LocalDate today = LocalDate.now();
        List<NutritionHistory> todaysNutritions = nutritionHistoryRepository.findByDate(today);
        if (todaysNutritions == null){
            todaysNutritions = new ArrayList<>();
        }
        
        Double totalEnergy = 0.0;
        Double totalProtein = 0.0;
        Double totalFat = 0.0;
        Double totalCholesterol = 0.0;
        Double totalCarbohydrates = 0.0;
        
        for(NutritionHistory nutrition : todaysNutritions) {
            totalEnergy += nutrition.getEnergy();
            totalProtein += nutrition.getProtein();
            totalFat += nutrition.getFat();
            totalCholesterol += nutrition.getCholesterol();
            totalCarbohydrates += nutrition.getCarbohydrates();
        }
        
        Nutrition totalNutrition = new Nutrition();
        totalNutrition.setEnergy(totalEnergy);
        totalNutrition.setProtein(totalProtein);
        totalNutrition.setFat(totalFat);
        totalNutrition.setCholesterol(totalCholesterol);
        totalNutrition.setCarbohydrates(totalCarbohydrates);
        logger.info("Total nutrition for today calculated.");
        return totalNutrition;
    }

}