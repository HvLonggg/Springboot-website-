package com.example.btl_cnjava.repository;

import com.example.btl_cnjava.entity.CartItem;
import com.example.btl_cnjava.entity.Snack;
import com.example.btl_cnjava.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndSnack(User user, Snack snack);
}