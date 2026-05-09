package com.hotel.demo.controller;

import com.hotel.demo.entity.RoomType;
import com.hotel.demo.entity.Room;
import com.hotel.demo.service.RoomService;
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

    @GetMapping("/room-types")
    public String listRoomTypes(Model model) {
        model.addAttribute("roomTypes", roomService.getAllRoomTypes());
        return "manager/room-type-list"; // Đường dẫn tới file HTML
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
    
    @GetMapping("/rooms")
    public String listRooms(Model model) {
    	model.addAttribute("rooms" , roomService.getAllRooms());
    	return "manager/room-list";
    }
    
    @GetMapping ("rooms/add")
    public String  showAddRoomForm (Model model) {
    	model.addAttribute("room", new Room());
    	model.addAttribute("roomTypes", roomService.getAllRoomTypes());
    	return "manager/room-form";
    }
    
    @PostMapping("/rooms/save")
    public String saveRoom(@ModelAttribute("room") Room room) {
        roomService.saveRoom(room);
        return "redirect:/manager/rooms";
    }
    
    @GetMapping ("/room-types/edit/{id}")
    public String showEditRoomTypeForm (@PathVariable("id") Long id , Model model) {
    	RoomType roomType = roomService.getRoomTypeById(id);
        model.addAttribute("roomType", roomType);
        return "manager/room-type-form"; // Dùng lại chung form với trang Add
    }
    
    @GetMapping ("/room-types/delete/{id}")
    public String deleteRoomType (@PathVariable("id") Long id) {
    	roomService.deleteRoomType(id);
    	return "redirect:/manager/room-types";
    }
    
    @GetMapping ("/rooms/edit/{id}")
    public String  showEditRoomForm (@PathVariable ("id") Long id , Model model) {
    	Room room = roomService.getRoomById(id);
    	model.addAttribute("room", room);
    	//Gửi thêm danh sách loại phòng
    	model.addAttribute ("roomTypes" , roomService.getAllRooms());
    	return "manager/room-form" ;
    }
    
    @GetMapping ("/rooms/delete/{id}")
    public String deleteRoom (@PathVariable ("id") Long id) {
    	roomService.deleteRoom(id);
    	return "redirect:/manager/rooms";
    }
    
    
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Lấy danh sách để đếm số lượng
        List<Room> rooms = roomService.getAllRooms();
        List<RoomType> roomTypes = roomService.getAllRoomTypes();

        // Gửi các con số thống kê sang giao diện
        model.addAttribute("totalRooms", rooms.size());
        model.addAttribute("totalRoomTypes", roomTypes.size());
        
        // Tạm thời tính tổng giá trị các phòng hiện có (Revenue Potential)
        double totalValue = roomTypes.stream().mapToDouble(RoomType::getPricePerNight).sum();
        model.addAttribute("potentialRevenue", totalValue);

        return "manager/dashboard";
    }
    
 
}