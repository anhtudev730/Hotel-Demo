package com.hotel.demo.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotel.demo.entity.Booking;
import com.hotel.demo.entity.Room;
import com.hotel.demo.entity.User;
import com.hotel.demo.repository.RoomRepository;
import com.hotel.demo.repository.UserRepository;
import com.hotel.demo.service.BookingService;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Hiển thị trang đặt phòng
    @GetMapping("/{roomId}")
    public String showBookingForm(@PathVariable Long roomId, Model model) {
        // Sử dụng hàm truy vấn có JOIN FETCH để đảm bảo RoomType luôn đi kèm
        Room room = roomRepository.findByIdWithRoomType(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng id: " + roomId));
        
        Booking booking = new Booking();
        booking.setRoom(room);
        
        model.addAttribute("booking", booking);
        model.addAttribute("room", room);
        return "user/booking-form";
    }

    // 2. Xử lý lưu đơn đặt phòng
    @PostMapping("/save")
    public String saveBooking(@ModelAttribute("booking") Booking booking, 
                              Principal principal, 
                              Model model) {
        try {
            // Lấy ID phòng từ object booking được gửi lên
            Long roomId = booking.getRoom().getId();
            
            // Tìm lại Room chuẩn từ DB để đảm bảo có đầy đủ thông tin (tránh lỗi null ở UI)
            Room roomFromDb = roomRepository.findByIdWithRoomType(roomId)
                    .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));

            // Gán lại room đầy đủ vào booking trước khi xử lý tiếp
            booking.setRoom(roomFromDb);

            // Lấy User đang đăng nhập
            String username = principal.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
            booking.setUser(user);
            
            // Gán trạng thái mặc định
            booking.setStatus("PENDING");

            // Gọi service xử lý (lưu xuống DB, tính giá, check trùng lịch...)
            bookingService.saveBooking(booking);
            
            return "redirect:/booking/my-bookings?success";

        } catch (Exception e) {
            // Nạp lại dữ liệu phòng đầy đủ để Thymeleaf render lại không bị lỗi
            if (booking.getRoom() != null && booking.getRoom().getId() != null) {
                Room roomRedo = roomRepository.findByIdWithRoomType(booking.getRoom().getId()).orElse(null);
                model.addAttribute("room", roomRedo);
            }
            
            model.addAttribute("booking", booking);
            model.addAttribute("error", e.getMessage());
            return "user/booking-form";
        }
    }

 // 3. Xem danh sách phòng đã đặt của tôi
    @GetMapping("/my-bookings")
    public String myBookings(Principal principal, Model model) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            
            String username = principal.getName();
            var listBookings = bookingService.findByUsername(username);
            
            model.addAttribute("bookings", listBookings);
            return "user/my-bookings"; // Đảm bảo bạn CÓ file này trong folder templates/user/
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải danh sách: " + e.getMessage());
            return "error"; // Hoặc một trang thông báo lỗi chung
        }
    }
}