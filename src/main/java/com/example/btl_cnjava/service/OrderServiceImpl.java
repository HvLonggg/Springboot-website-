package com.example.btl_cnjava.service;

import com.example.btl_cnjava.entity.*;
import com.example.btl_cnjava.repository.OrderRepository;
import com.example.btl_cnjava.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrderFromCartItems(User user, List<CartItem> cartItems, String paymentMethod) {
        Order order = new Order();
        order.setUser(user);
        order.setPaymentMethod(paymentMethod);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setSnack(cartItem.getSnack());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(cartItem.getSnack().getPrice()));

            total = total.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(total);

        return order;
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
    @Override
    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByUserUsername(username);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
}