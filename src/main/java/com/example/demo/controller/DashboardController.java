import com.example.demo.model.Nutrition;
import com.example.demo.repository.NutritionRepository;
import com.example.demo.service.NutritionService;
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
@ public class DashboardController {
    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private NutritionService nutritionService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        List<Nutrition> foods = nutritionRepository.findAll();
        model.addAttribute("foods", foods);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
        }

        return "dashboard";
    }

    @PostMapping("/dashboard")
    public String postDashboard(@RequestParam String food, @RequestParam double grams, Model model) {
        Nutrition selectedNutrition = nutritionService.getNutritionByFoodName(food);
        Nutrition calculatedNutrition = nutritionService.calculateNutritionForGrams(selectedNutrition, grams);
        model.addAttribute("selectedNutrition", calculatedNutrition);

        List<Nutrition> foods = nutritionRepository.findAll();
        model.addAttribute("foods", foods);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
        }
        

        return "dashboard";
    }
}

