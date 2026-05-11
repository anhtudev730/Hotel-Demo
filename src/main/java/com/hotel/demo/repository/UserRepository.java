package com.hotel.demo.repository;

import com.hotel.demo.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm kiếm người dùng theo username để phục vụ đăng nhập
    Optional<User> findByUsername(String username);
    
 // Tìm kiếm username hoặc email có chứa từ khóa (không phân biệt hoa thường)
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'ROLE_USER'")
    Long countTotalCustomers();
}