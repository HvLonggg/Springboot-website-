package com.example.btl_cnjava.service;

import com.example.btl_cnjava.entity.Order;
import com.example.btl_cnjava.entity.OrderStatus;
import com.example.btl_cnjava.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrdersSortedByDateDesc() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng có id = " + id));
    }

    public void updateOrderStatus(Long id, String statusStr) {
        Order order = getOrderById(id);

        // Convert String sang enum
        OrderStatus status = OrderStatus.valueOf(statusStr.toUpperCase());

        order.setStatus(status);
        orderRepository.save(order);
    }
}
