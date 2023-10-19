package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {


    @GetMapping("/login")
    public String getLogin(Model model) {

        return "login";
    }

@Autowired
private UserService userService; // UserServiceはデータベースからユーザー情報を取得するためのクラスとします。

@PostMapping("/login")
public String postLogin(@RequestParam String username, @RequestParam String password, Model model) {

    User user = userService.findByUsername(username); // データベースからユーザー情報を取得

    if (user != null && password.equals(user.getPassword())) {
        return "/dashboard"; 
    } else {
        model.addAttribute("errorMessage", "ユーザー名またはパスワードが間違っています。");
        return "login"; 
    }
}

}
 30 changes: 30 additions & 0 deletions30 