package com.hotel.demo.controller;

import com.hotel.demo.entity.RoomType;
import com.hotel.demo.entity.Room;
import com.hotel.demo.entity.Booking;
import com.hotel.demo.service.RoomService;
import com.hotel.demo.repository.BookingRepository;
import com.hotel.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    // ==========================================
    // QUẢN LÝ LOẠI PHÒNG (ROOM TYPE)
    // ==========================================
    @GetMapping("/room-types")
    public String listRoomTypes(Model model) {
        model.addAttribute("roomTypes", roomService.getAllRoomTypes());
        return "manager/room-type-list";
    }

    @GetMapping("/room-types/add")
    public String showAddForm(Model model) {
        model.addAttribute("roomType", new RoomType());
        return "manager/room-type-form";
    }

    @PostMapping("/room-types/save")
    public String saveRoomType(@ModelAttribute("roomType") RoomType roomType) {
        roomService.saveRoomType(roomType);
        return "redirect:/manager/room-types";
    }

    @GetMapping("/room-types/edit/{id}")
    public String showEditRoomTypeForm(@PathVariable("id") Long id, Model model) {
        RoomType roomType = roomService.getRoomTypeById(id);
        model.addAttribute("roomType", roomType);
        return "manager/room-type-form";
    }

    @GetMapping("/room-types/delete/{id}")
    public String deleteRoomType(@PathVariable("id") Long id) {
        roomService.deleteRoomType(id);
        return "redirect:/manager/room-types";
    }

    // ==========================================
    // QUẢN LÝ PHÒNG (ROOM)
    // ==========================================
    @GetMapping("/rooms")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "manager/room-list";
    }

    @GetMapping("/rooms/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("roomTypes", roomService.getAllRoomTypes());
        return "manager/room-form";
    }

    @PostMapping("/rooms/save")
    public String saveRoom(@ModelAttribute("room") Room room) {
        roomService.saveRoom(room);
        return "redirect:/manager/rooms";
    }

    @GetMapping("/rooms/edit/{id}")
    public String showEditRoomForm(@PathVariable("id") Long id, Model model) {
        Room room = roomService.getRoomById(id);
        model.addAttribute("room", room);
        // SỬA LỖI: Lấy danh sách LOẠI PHÒNG cho dropdown, không phải danh sách PHÒNG
        model.addAttribute("roomTypes", roomService.getAllRoomTypes());
        return "manager/room-form";
    }

    @GetMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable("id") Long id) {
        roomService.deleteRoom(id);
        return "redirect:/manager/rooms";
    }

    // ==========================================
    // DASHBOARD THỐNG KÊ (ANALYTICS)
    // ==========================================
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // 1. Thống kê cơ bản về cơ sở vật chất
        model.addAttribute("totalRooms", roomService.getAllRooms().size());
        model.addAttribute("totalRoomTypes", roomService.getAllRoomTypes().size());
        
        // 2. Thống kê doanh thu (Gọi từ Query SUM trong BookingRepository)
        Double totalRevenue = bookingRepository.getTotalRevenue();
        model.addAttribute("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        
        // 3. Thống kê tình trạng đơn hàng
        model.addAttribute("totalBookings", bookingRepository.countTotalBookings());
        model.addAttribute("pendingBookings", bookingRepository.countPendingBookings());
        
        // 4. Thống kê lượng khách hàng
        model.addAttribute("totalUsers", userRepository.count());

        // 5. Hiển thị 5 đơn đặt phòng mới nhất
        List<Booking> recentBookings = bookingRepository.findTop5ByOrderByIdDesc();
        model.addAttribute("recentBookings", recentBookings);

        return "manager/dashboard";
    }
}