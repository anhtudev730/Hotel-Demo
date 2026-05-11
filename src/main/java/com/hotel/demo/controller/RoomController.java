package com.hotel.demo.controller;

import com.hotel.demo.entity.Room;
import com.hotel.demo.repository.RoomRepository;
import com.hotel.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate; // Để sửa lỗi gạch đỏ LocalDate
import org.springframework.format.annotation.DateTimeFormat; // Để sửa lỗi gạch đỏ @DateTimeFormat


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
    
    @GetMapping("/search-available")
    public String searchAvailableRooms(
            @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            Model model) {
        
        // Kiểm tra nếu ngày check-out trước ngày check-in thì báo lỗi
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            model.addAttribute("error", "Ngày trả phòng phải sau ngày nhận phòng!");
            return "user/room-list";
        }

        List<Room> availableRooms = roomRepository.findAvailableRooms(checkIn, checkOut);
        
        model.addAttribute("rooms", availableRooms);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        return "user/room-list";
    }
}