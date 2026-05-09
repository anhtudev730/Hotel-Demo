package com.hotel.demo.controller;

import com.hotel.demo.entity.User;
import com.hotel.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }
    
    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // Xử lý lưu người dùng mới
    @PostMapping("/register")
    public String processRegister(String username, String email, String password) {
        // 1. Kiểm tra xem username đã tồn tại chưa (Bước này giúp hệ thống ổn định hơn)
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error";
        }

        // 2. Tạo đối tượng User mới
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        
        // MÃ HÓA mật khẩu trước khi lưu (Bắt buộc để Spring Security đọc được)
        newUser.setPassword(passwordEncoder.encode(password));
        
        // Gán quyền mặc định là ROLE_USER (Khớp với cấu hình SecurityConfig)
        newUser.setRole("ROLE_USER");
        
        // 3. Lưu vào Database
        userRepository.save(newUser);
        
        // 4. Điều hướng về trang Login kèm thông báo thành công
        return "redirect:/login?success";
    }
    
    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String handleChangePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            java.security.Principal principal, 
            org.springframework.ui.Model model) {

        // Lấy thông tin user đang đăng nhập qua Principal
        String username = principal.getName();
        com.hotel.demo.entity.User user = userRepository.findByUsername(username).get();

        // KIỂM TRA 1: Mật khẩu cũ phải khớp với bản mã hóa trong DB
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("error", "Mật khẩu hiện tại không chính xác!");
            return "change-password";
        }

        // KIỂM TRA 2: Mật khẩu mới và xác nhận phải giống nhau
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Xác nhận mật khẩu mới không khớp!");
            return "change-password";
        }

        // LƯU: Mã hóa mật khẩu mới trước khi lưu lại
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("success", "Chúc mừng! Bạn đã đổi mật khẩu thành công.");
        return "change-password";
    }
}