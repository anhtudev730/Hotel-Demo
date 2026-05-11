package com.hotel.demo.repository;

import com.hotel.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Tìm các đơn đặt phòng của một User cụ thể
	List<Booking> findByUserUsername(String username);
    // LOGIC: Kiểm tra xem phòng có bị bận trong khoảng thời gian khách chọn không
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
           "WHERE b.room.id = :roomId " +
           "AND b.status != 'CANCELLED' " + 
           "AND (:checkIn < b.checkOutDate AND :checkOut > b.checkInDate)")
    boolean isRoomOccupied(@Param("roomId") Long roomId, 
                          @Param("checkIn") LocalDate checkIn, 
                          @Param("checkOut") LocalDate checkOut);
   
    
 // 1. Tính tổng doanh thu
    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.status = 'CONFIRMED'")
    Double getTotalRevenue();

    // 2. Đếm tổng số đơn hàng
    @Query("SELECT COUNT(b) FROM Booking b")
    long countTotalBookings();

    // 3. Đếm đơn hàng đang chờ xử lý (Sửa lỗi dòng 114 của bạn)
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'PENDING'")
    long countPendingBookings();

    // 4. Lấy 5 đơn mới nhất (Sửa lỗi dòng 120 của bạn)
    List<Booking> findTop5ByOrderByIdDesc();
}