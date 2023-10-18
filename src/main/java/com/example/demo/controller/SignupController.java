package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.service.UserService;
import com.example.demo.model.User;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;  // 追加

    @GetMapping("/signup")
    public String getSignup(Model model) {
        model.addAttribute("user", new User());  // 空のユーザーオブジェクトをビューに追加
        return "signup";
    }

    @PostMapping("/signup")
    public String postSignup(@ModelAttribute User user, Model model) {
        userService.save(user);  // ユーザーを保存
        return "dashboard";
    }
}
