package com.example.demo.controller;

import com.example.demo.model.Nutrition;
import com.example.demo.repository.NutritionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class DashboardController {

    @Autowired
    private NutritionRepository nutritionRepository;

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
}
