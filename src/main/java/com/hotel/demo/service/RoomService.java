package com.hotel.demo.service;

import com.hotel.demo.entity.RoomType;
import com.hotel.demo.entity.Room;
import com.hotel.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.hotel.demo.repository.RoomRepository;

@Service
public class RoomService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    public void saveRoomType(RoomType roomType) {
        roomTypeRepository.save(roomType);
    }
    
    @Autowired
    private RoomRepository roomRepository;
    
    public List <Room> getAllRooms (){
    	return roomRepository.findAll();
    }
    
    public void saveRoom (Room room) {
    	roomRepository.save(room);
    }
    
    public RoomType getRoomTypeById(Long id) {
    	return roomTypeRepository.findById(id).orElse(null);
    }
    
    public void deleteRoomType (Long id) {
    	roomTypeRepository.deleteById(id);
    }
    
    // Tìm phòng theo ID 
    
    public Room getRoomById (Long id) {
    	return roomRepository.findById(id).orElse(null);
    }
    
    public  void  deleteRoom (Long id) {
    	roomRepository.deleteById(id);
    }
}