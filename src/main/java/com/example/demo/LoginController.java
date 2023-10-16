package com.example.demo;

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

 @PostMapping("/login")
    public String postLogin(@RequestParam String username, @RequestParam String password, Model model) {
        // シンプルな認証の例: "admin" と "secret" がユーザ名とパスワード
        if ("admin".equals(username) && "secret".equals(password)) {
            return "redirect:/dashboard";  // 認証成功時、ホームページにリダイレクト
        } else {
            model.addAttribute("errorMessage", "ユーザー名またはパスワードが間違っています。");
            return "login";  // 認証失敗時、エラーメッセージとともにログインページに戻る
        }
    }

  
}
