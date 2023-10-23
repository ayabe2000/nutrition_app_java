package com.example.demo.controller;
import com.example.demo.model.Nutrition;
import com.example.demo.repository.NutritionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.demo.service.NutritionService;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private NutritionService nutritionService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        populateDashboardModel(model);
        return "dashboard";
    }

   @PostMapping("/dashboard")
    public String postDashboard(
    @RequestParam String food,
    @RequestParam double grams, 
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, 
    Model model) {
    LocalDate today = LocalDate.now();
    Nutrition baseNutrition = nutritionService.getNutritionByFoodName(food);
    
    String username = getAuthenticatedUsername(); // ユーザー名を取得

    // 新しいNutritionオブジェクトを作成し、食品名とグラム数を設定
    Nutrition calculatedNutrition = new Nutrition();
    calculatedNutrition.setFoodName(food);
    calculatedNutrition.setGrams(grams);
            
    if(baseNutrition == null) {
        model.addAttribute("errorMessage", "選択した食品が見つかりませんでした。");
    } else {
        calculatedNutrition = nutritionService.calculateNutritionForGrams(calculatedNutrition, grams);
        if (calculatedNutrition == null) {
            model.addAttribute("errorMessage","栄養計算エラー");
        } else {
            nutritionService.addNutritionForDate(calculatedNutrition, date, username);
            String message = date.equals(today) ? "今日の栄養素に成功的に追加されました！" : date + "の栄養素の履歴に成功的に追加されました！";
            model.addAttribute("successMessage", message);

            List<Nutrition> nutritionList = new ArrayList<>();
            nutritionList.add(calculatedNutrition);
            model.addAttribute("nutritionList", nutritionList);
        }
    }
    
    populateDashboardModel(model); // モデルを更新
    return "dashboard";
}

private void populateDashboardModel(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today);

        List<Nutrition> foods = nutritionService.findAllFoods();
        model.addAttribute("foods", foods);

        String username = getAuthenticatedUsername();
        if (username != null) {
            model.addAttribute("username", username);
            List<Nutrition> nutritionHistoryData = nutritionService.getPastNutritionHistoryForUser(username);
        
            List<String> foodNames = new ArrayList<>();
            List<Double> gramsList = new ArrayList<>();
            
            for (Nutrition nutrition : nutritionHistoryData) {
                foodNames.add(nutrition.getFoodName());
                gramsList.add(nutrition.getGrams());
            }
            
            model.addAttribute("foodNamesHistory", foodNames);
            model.addAttribute("gramsListHistory", gramsList);
            model.addAttribute("nutritionHistory", nutritionHistoryData);
        }

        Nutrition todayTotalNutrition = nutritionService.getTodayTotalNutrition();
    model.addAttribute("todayTotalNutrition", todayTotalNutrition);

    if(todayTotalNutrition != null) {  
        logger.info("Energy value of todayTotalNutrition: " + todayTotalNutrition.getEnergy());
    }
}

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }
}