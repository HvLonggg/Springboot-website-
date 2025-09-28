package com.example.btl_cnjava.repository;

import com.example.btl_cnjava.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Tìm đơn hàng theo user
    List<Order> findByUserId(Long userId);

    // Tìm theo trạng thái
    List<Order> findByStatus(String status);
    List<Order> findAllByOrderByOrderDateDesc();
    List<Order> findByUserUsername(String username);

}
