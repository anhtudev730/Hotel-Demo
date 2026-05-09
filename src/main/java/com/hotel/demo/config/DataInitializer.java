package com.hotel.demo.config;

import com.hotel.demo.entity.User;
import com.hotel.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Kiểm tra nếu chưa có Admin thì tạo mới
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                // Quan trọng: Phải mã hóa mật khẩu trước khi lưu
                admin.setPassword(passwordEncoder.encode("123456")); 
                admin.setEmail("admin@hotel.com");
                admin.setRole("ROLE_ADMIN");
                userRepository.save(admin);
                System.out.println(">>> Đã tạo tài khoản Admin mặc định: admin/123456");
            }

            // 2. Tạo thêm một tài khoản User mẫu để test phân quyền
            if (userRepository.findByUsername("khachhang").isEmpty()) {
                User user = new User();
                user.setUsername("khachhang");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setEmail("user@gmail.com");
                user.setRole("ROLE_USER");
                userRepository.save(user);
                System.out.println(">>> Đã tạo tài khoản User mẫu: khachhang/123456");
            }
        };
    }
}