package com.example.btl_cnjava.controller;

import com.example.btl_cnjava.entity.*;
import com.example.btl_cnjava.repository.OrderRepository;
import com.example.btl_cnjava.service.OrderService;
import com.example.btl_cnjava.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/user-orders")
    public String userOrders(Model model, Principal principal) {
        String username = principal.getName();
        List<Order> orders = orderService.getOrdersByUsername(username);
        model.addAttribute("orders", orders);
        return "user-orders"; // file Thymeleaf
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String paymentMethod, Principal principal, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Order order = orderService.createOrderFromCartItems(user, cart, paymentMethod);
        session.removeAttribute("cart");

        return "redirect:/orders/" + order.getId(); // Hoặc đổi thành "/order/user-orders" tùy bạn
    }

    @PostMapping("/cancel/{orderId}")
    public String cancelOrder(@PathVariable Long orderId, Principal principal, RedirectAttributes redirectAttributes) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đơn hàng không tồn tại.");
            return "redirect:/order/user-orders";
        }

        String username = principal.getName();
        if (!order.getUser().getUsername().equals(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền hủy đơn này.");
            return "redirect:/order/user-orders";
        }

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chỉ có thể hủy đơn khi trạng thái chờ xác nhận.");
            return "redirect:/order/user-orders";
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderService.save(order);

        redirectAttributes.addFlashAttribute("successMessage", "Đơn hàng đã được hủy thành công.");
        return "redirect:/order/user-orders";
    }

}
