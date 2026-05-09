package com.hotel.demo.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với người dùng đang đặt phòng
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Liên kết với phòng được đặt
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
    
    private Double totalPrice;
    
    private String status; // Các trạng thái: PENDING, CONFIRMED, CANCELLED
    
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public LocalDate getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

   
}