package com.hotel.demo.repository;

import com.hotel.demo.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    // JpaRepository đã cung cấp sẵn các hàm save(), findAll(), deleteById()
}