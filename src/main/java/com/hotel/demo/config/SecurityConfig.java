package com.hotel.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                // 1. Cho phép tất cả mọi người truy cập tài nguyên tĩnh và các trang cơ bản
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                
                // 2. Cấp quyền cho cả USER và ADMIN được phép vào chức năng đặt phòng
                .requestMatchers("/booking/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                
                // 3. Chỉ ADMIN mới được vào các trang quản trị
                .requestMatchers("/manager/**").hasAuthority("ROLE_ADMIN")
                
                // 4. Các yêu cầu còn lại bắt buộc phải đăng nhập
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                // Quan trọng: Để false để Spring tự điều hướng về trang chủ hoặc trang người dùng đang xem trước đó
                .defaultSuccessUrl("/", false) 
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}