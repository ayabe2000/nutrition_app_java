package com.example.demo;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .formLogin(login -> login
                    .loginProcessingUrl("/login")
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard")
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutSuccessUrl("/login")
                )
                .authorizeHttpRequests(authz -> authz
                    .requestMatchers( "/login","/signup","/dashboard", "/css/**", "/js/**", "/images/**").permitAll()
                    .anyRequest().authenticated()
                );
            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
}