package com.hotel.demo.repository;

import com.hotel.demo.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Cách này sẽ "ép" Hibernate lấy luôn RoomType cùng lúc với Room bằng 1 câu lệnh JOIN
    @Query("SELECT r FROM Room r JOIN FETCH r.roomType WHERE r.id = :id")
    Optional<Room> findByIdWithRoomType(@Param("id") Long id);
}