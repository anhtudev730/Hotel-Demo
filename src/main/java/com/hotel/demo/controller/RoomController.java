package com.hotel.demo.controller;

import com.hotel.demo.entity.Room;
import com.hotel.demo.repository.RoomRepository;
import com.hotel.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    // Trang chủ hoặc danh sách phòng kèm tìm kiếm
    @GetMapping({"/list"})
    public String searchRooms(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model model) {
        
        // Gọi hàm searchRooms đã thêm vào RoomRepository
        List<Room> list = roomRepository.searchRooms(name, typeId, minPrice, maxPrice);
        
        model.addAttribute("rooms", list);
        model.addAttribute("roomTypes", roomTypeRepository.findAll());
        return "user/room-list"; // Trang hiển thị danh sách phòng
    }
}