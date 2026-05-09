package com.hotel.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hotel.demo.entity.Booking;
import com.hotel.demo.repository.BookingRepository;
import com.hotel.demo.service.BookingService;

import java.util.List;
@Controller
@RequestMapping("/manager/bookings")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    // Trang danh sách cho Admin
    @GetMapping("/list")
    public String listAllBookings(Model model) {
        model.addAttribute("bookings", bookingRepository.findAll());
        return "manager/booking-list"; // Đây là tên file HTML tí nữa mình tạo
    }

    // Link khi nhấn nút Xác nhận
    @GetMapping("/confirm/{id}")
    public String confirm(@PathVariable Long id) {
        bookingService.confirmBooking(id);
        return "redirect:/manager/bookings/list?confirmed";
    }

    // Link khi nhấn nút Hủy
    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/manager/bookings/list?cancelled";
    }
    
    // Học git cơ bản.
}