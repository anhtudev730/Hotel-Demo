package com.hotel.demo.service;

import com.hotel.demo.entity.Booking;
import com.hotel.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking saveBooking(Booking booking) {
        // 1. Kiểm tra ngày hợp lệ
        if (!booking.getCheckOutDate().isAfter(booking.getCheckInDate())) {
            throw new RuntimeException("Ngày trả phòng phải sau ngày nhận phòng!");
        }

        // 2. Kiểm tra phòng có trống không (Logic của Anh Tú)
        boolean isOccupied = bookingRepository.isRoomOccupied(
                booking.getRoom().getId(), 
                booking.getCheckInDate(), 
                booking.getCheckOutDate());
        
        if (isOccupied) {
            throw new RuntimeException("Xin lỗi, phòng này đã được đặt trong khoảng thời gian bạn chọn!");
        }

        // 3. Tính tiền tự động (Logic của mình)
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        double pricePerNight = booking.getRoom().getRoomType().getPricePerNight();
        booking.setTotalPrice(nights * pricePerNight);

        // 4. Thiết lập trạng thái mặc định và lưu
        booking.setStatus("PENDING");
        return bookingRepository.save(booking);
    }
    
    public List<Booking> findByUsername(String username) {
        // Gọi xuống Repository để lấy danh sách theo username
        return bookingRepository.findByUserUsername(username);
    }
    
 // Hàm để Admin nhấn xác nhận
    public void confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt phòng"));
        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);
    }

    // Hàm để Admin nhấn hủy
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt phòng"));
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }
}