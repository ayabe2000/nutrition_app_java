package com.example.demo.controller;

import com.example.demo.model.Nutrition;
import com.example.demo.model.NutritionHistory;
import com.example.demo.repository.NutritionDailySummary;
import com.example.demo.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private NutritionService nutritionService;

    

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {

        logger.debug("GET ダッシュボードフォームにアクセスしました。");
        //ダッシュボードのモデルデータを設定。
        populateDashboardModel(model);
        // 当日の栄養情報を取得。
        Nutrition todayTotalNutrition = (Nutrition) model.getAttribute("todayTotalNutrition");
        if (todayTotalNutrition != null) {
            logger.info("Energy value of todayTotalNutrition: {}", todayTotalNutrition.getEnergy());
     }

        return "dashboard";
    }
    // ダッシュボードのモデルデータを設定するメソッド
    private void populateDashboardModel(Model model) {
        //全ての食品のリストを取得
        List<Nutrition> allFoods = nutritionService.getAllFoods();
        model.addAttribute("foods", allFoods);

        // 現在の日付を取得。
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today);

        // 認証済みのユーザー名を取得。
        String username = getAuthenticatedUsername();
        if (username != null) {
            model.addAttribute("username", username);

            
        // 特定のユーザーの過去の栄養履歴を取得
        List<NutritionDailySummary> nutritionSummaryList = nutritionService.getPastNutritionDailySummaryForUser(username);
        logger.info("Fetched {} nutrition summary records for user: {}", nutritionSummaryList.size(), username);

        // nullのオブジェクトをフィルタリング
        nutritionSummaryList = nutritionSummaryList.stream().filter(Objects::nonNull).collect(Collectors.toList());

        // リストの初期化
        List<String> datesList = new ArrayList<>();
        List<String> foodNames = new ArrayList<>();
        List<Double> gramsList = new ArrayList<>();
        List<Double> energyList = new ArrayList<>();
        List<Double> proteinList = new ArrayList<>();
        List<Double> fatList = new ArrayList<>();
        List<Double> cholesterolList = new ArrayList<>();
        List<Double> carbohydratesList = new ArrayList<>();

        // 過去の栄養履歴から日付、食品名、グラム、および各栄養素を取得
        for (NutritionDailySummary summary : nutritionSummaryList) {
            datesList.add(summary.getDate().toString()); 
            gramsList.add(summary.getGrams());
            foodNames.add(summary.getFoodName());
            energyList.add(summary.getEnergySum());
            proteinList.add(summary.getProteinSum());
            fatList.add(summary.getFatSum());
            cholesterolList.add(summary.getCholesterolSum());
            carbohydratesList.add(summary.getCarbohydratesSum());

            logger.info("Processed nutrition record for food: {}", summary.getFoodName());
        }

        List<NutritionDailySummary> summaries = nutritionService.getDailySummaries();
        model.addAttribute("summaries", summaries);


        // モデルに追加
        model.addAttribute("datesListHistory", datesList);
        model.addAttribute("foodNamesHistory", foodNames);
        model.addAttribute("gramsListHistory", gramsList);
        model.addAttribute("energyListHistory", energyList);
        model.addAttribute("proteinListHistory", proteinList);
        model.addAttribute("fatListHistory", fatList);
        model.addAttribute("cholesterolListHistory", cholesterolList);
        model.addAttribute("carbohydratesListHistory", carbohydratesList);
        model.addAttribute("nutritionHistory", nutritionSummaryList);
        logger.info("Data added to the model successfully.");
       }
       
        // 当日の合計栄養情報を取得してモデルに追加
        Nutrition todayTotalNutrition = nutritionService.getTodayTotalNutrition();

        if (todayTotalNutrition != null) {
            logger.info("Setting todayTotalNutrition to model: {}", todayTotalNutrition);
        } else {
            logger.warn("todayTotalNutrition is null");
        }

        model.addAttribute("todayTotalNutrition", todayTotalNutrition);
    }
    

    @PostMapping("/dashboard")
    //ユーザーがダッシュボードフォームからデータを送信するときに呼び出されるメソッド
    public String postDashboard(
        @NotNull @NotEmpty @RequestParam(required = true) String food,
        @RequestParam double grams, 
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, 
        Model model) {
            logger.debug("Handling POST request for dashboard with food: {}", food);

        try {
            logger.info("Received food parameter: {}", food);
            LocalDate today = LocalDate.now();
            
            // 食品名に基づいて基本栄養情報を取得
            Nutrition baseNutrition = nutritionService.getNutritionByFoodName(food);
            if(baseNutrition == null) {
                model.addAttribute("errorMessage", "選択した食品が見つかりませんでした。");
                populateDashboardModel(model);
                return "dashboard";
            }

            // 認証済みのユーザー名を取得
            String username = getAuthenticatedUsername(); 
            logger.debug("Fetching authenticated username");
            // 指定されたグラム数に基づいて栄養情報を計算
            NutritionHistory calculatedNutritionHistory = nutritionService.calculateNutritionForGrams(baseNutrition, grams);

            if(calculatedNutritionHistory == null) {
                model.addAttribute("errorMessage", "栄養計算エラー");
                populateDashboardModel(model);
                return "dashboard";
            } 

            logger.info("Inserting nutrition data with parameters - Food Name: {}, Grams: {}, Date: {}, Username: {}", food, grams, date, username);

                    
            // 栄養データをデータベースに保存
            nutritionService.addNutritionForDate(calculatedNutritionHistory, date, username);
            logger.info("Successfully saved nutrition data for user: {}, food: {}, date: {}", username, food, date);
            String message = date.equals(today) ? "今日の栄養素に成功的に追加されました！" : date + "の栄養素の履歴に成功的に追加されました！";
            System.out.println(message);

            model.addAttribute("successMessage", message);
            model.addAttribute("nutritionList", Arrays.asList(convertToNutrition(calculatedNutritionHistory)));

            populateDashboardModel(model);
            logger.debug("POST ダッシュボードフォームが送信されました。");
            return "dashboard";

        } catch (Exception e) {
            logger.error("ダッシュボードのリクエスト処理中にエラーが発生しました。", e);
            model.addAttribute("errorMessage", "内部エラーが発生しました。");
            populateDashboardModel(model); 
            return "dashboard";
        }
    }


   // 現在の認証済みユーザーのユーザー名を取得するメソッド
    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }
    
    // NutritionHistoryオブジェクトをNutritionオブジェクトに変換するメソッド。
    private Nutrition convertToNutrition(NutritionHistory history) {
        logger.debug("Converting NutritionHistory object to Nutrition object");
        Nutrition nutrition = new Nutrition();
      
        nutrition.setFoodName(history.getFoodName());
        nutrition.setFat(history.getFat());
        nutrition.setCholesterol(history.getCholesterol());
        nutrition.setCarbohydrates(history.getCarbohydrates());
        return nutrition;
    }

}