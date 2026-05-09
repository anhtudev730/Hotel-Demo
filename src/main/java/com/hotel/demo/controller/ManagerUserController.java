package com.hotel.demo.controller;

import com.hotel.demo.entity.User;
import com.hotel.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manager/users") // Chỉ Admin mới vào được cụm này
public class ManagerUserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/manager/users?deleted";
    }
    
    @PostMapping("/update-role/{id}")
    public String updateUserRole(@PathVariable Long id, @RequestParam String role) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRole(role); // Cập nhật role mới (ROLE_ADMIN hoặc ROLE_USER)
        userRepository.save(user);
        return "redirect:/manager/users?updated";
    }
    
    @GetMapping
    public String listUsers(@RequestParam(required = false) String keyword, Model model) {
        List<User> users;
        if (keyword != null && !keyword.isEmpty()) {
            // Nếu có từ khóa, thực hiện tìm kiếm
            users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
            model.addAttribute("keyword", keyword); // Gửi lại từ khóa để hiển thị trên ô nhập
        } else {
            // Nếu không có, hiển thị tất cả
            users = userRepository.findAll();
        }
        model.addAttribute("users", users);
        return "manager/user-list";
    }
}