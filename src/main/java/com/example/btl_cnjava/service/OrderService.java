package com.example.btl_cnjava.service;

import com.example.btl_cnjava.entity.CartItem;
import com.example.btl_cnjava.entity.Order;
import com.example.btl_cnjava.entity.User;

import java.util.List;

public interface OrderService {
    Order createOrderFromCartItems(User user, List<CartItem> cartItems, String paymentMethod);
    Order saveOrder(Order order);
    List<Order> getOrdersByUsername(String username);
    Order findById(Long id);
    Order save(Order order);
}
