package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DashboardController {

    
    @GetMapping("/dashboard")
    public String getLogin(Model model) {
    
        return "dashboard";
    }

}
