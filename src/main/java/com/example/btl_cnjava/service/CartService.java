package com.example.btl_cnjava.service;

import com.example.btl_cnjava.entity.CartItem;
import com.example.btl_cnjava.entity.Snack;
import com.example.btl_cnjava.entity.User;
import com.example.btl_cnjava.repository.CartItemRepository;
import com.example.btl_cnjava.repository.SnackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private SnackRepository snackRepository;

    public void addToCart(User user, Long snackId, int quantity) {
        Snack snack = snackRepository.findById(snackId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy snack"));

        Optional<CartItem> optionalCart = cartItemRepository.findByUserAndSnack(user, snack);
        if (optionalCart.isPresent()) {
            CartItem item = optionalCart.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setSnack(snack);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void clearCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(items);
    }

    public void removeItemById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
