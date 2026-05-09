package com.hotel.demo.controller;

import com.hotel.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private RoomRepository roomRepository; // 1. Phải có dòng này để lấy dữ liệu từ DB

    @GetMapping("/")
    public String index(Model model) {
        // 2. Lấy danh sách phòng và đặt tên là "rooms" để gửi sang HTML
        model.addAttribute("rooms", roomRepository.findAll()); 
        return "index";
    }
}