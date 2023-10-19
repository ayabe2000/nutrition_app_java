package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
  
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    // ユーザー名を指定してユーザーを取得
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // 新規ユーザーを保存
    public User save(User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // パスワードをエンコード
        return userRepository.save(user);
    }

    // IDを指定してユーザーを取得
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // IDを指定してユーザーを削除
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

     // ユーザー名とパスワードを指定してユーザーを認証
    public boolean authenticate(String username, String password, PasswordEncoder passwordEncoder) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordEncoder.matches(password, user.getPassword());  // エンコードされたパスワードと照合
        }
        return false;
    }
}