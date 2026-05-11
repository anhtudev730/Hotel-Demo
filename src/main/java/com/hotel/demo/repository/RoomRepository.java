package com.hotel.demo.repository;

import com.hotel.demo.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Cách này sẽ "ép" Hibernate lấy luôn RoomType cùng lúc với Room bằng 1 câu lệnh JOIN
    @Query("SELECT r FROM Room r JOIN FETCH r.roomType WHERE r.id = :id")
    Optional<Room> findByIdWithRoomType(@Param("id") Long id);
    
    @Query("SELECT r FROM Room r WHERE " +
            "(:name IS NULL OR LOWER(r.roomNumber) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:typeId IS NULL OR r.roomType.id = :typeId) AND " +
            "(:minPrice IS NULL OR r.roomType.pricePerNight >= :minPrice) AND " +
            "(:maxPrice IS NULL OR r.roomType.pricePerNight <= :maxPrice)")
     List<Room> searchRooms(@Param("name") String name, 
                            @Param("typeId") Long typeId, 
                            @Param("minPrice") Double minPrice, 
                            @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT r FROM Room r WHERE r.id NOT IN (" +
    	       "SELECT b.room.id FROM Booking b WHERE b.status != 'CANCELLED' AND (" +
    	       "(:checkIn BETWEEN b.checkInDate AND b.checkOutDate) OR " +
    	       "(:checkOut BETWEEN b.checkInDate AND b.checkOutDate) OR " +
    	       "(b.checkInDate BETWEEN :checkIn AND :checkOut)" +
    	       "))")
    	List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn, 
    	                              @Param("checkOut") LocalDate checkOut);
}