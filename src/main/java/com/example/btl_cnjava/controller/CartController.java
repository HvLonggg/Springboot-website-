package com.example.btl_cnjava.controller;

import com.example.btl_cnjava.entity.OrderStatus;
import com.example.btl_cnjava.entity.CartItem;
import com.example.btl_cnjava.entity.Order;
import com.example.btl_cnjava.entity.User;
import com.example.btl_cnjava.repository.OrderRepository;
import com.example.btl_cnjava.service.CartService;
import com.example.btl_cnjava.service.OrderService;
import com.example.btl_cnjava.service.SnackService;
import com.example.btl_cnjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SnackService snackService;

    @Autowired
    private UserService userService;




    // Hiển thị giỏ hàng
    @GetMapping
    public String showCart(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartService.getCartItems(user);

        BigDecimal total = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getSnack().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }

    // Thêm món vào giỏ
    @PostMapping("/add/{snackId}")
    public String addToCart(@PathVariable Long snackId,
                            @RequestParam(defaultValue = "1") int quantity,
                            Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartService.addToCart(user, snackId, quantity);
        return "redirect:/cart";
    }

    // Xóa món khỏi giỏ
    @PostMapping("/remove/{itemId}")
    public String removeFromCart(@PathVariable Long itemId, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        // Có thể kiểm tra quyền nếu cần, ví dụ item này có thuộc user không, hoặc bỏ qua nếu chắc chắn
        cartService.removeItemById(itemId);
        return "redirect:/cart";
    }


    // odder
    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String paymentMethod,
                             @RequestParam String recipientName,
                             @RequestParam String recipientPhone,
                             @RequestParam String deliveryAddress,
                             @RequestParam(required = false) String note,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        // Lấy user hiện tại từ username
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        List<CartItem> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {

            redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn đang trống!");
            return "redirect:/cart";
        }

        // Tạo đơn hàng từ giỏ hàng
        Order order = orderService.createOrderFromCartItems(user, cartItems, paymentMethod);

        // Ghi thông tin nhận hàng
        order.setRecipientName(recipientName);
        order.setRecipientPhone(recipientPhone);
        order.setDeliveryAddress(deliveryAddress);
        order.setNote(note);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        orderRepository.save(order);
        cartService.clearCart(user);

        redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
        return "redirect:/user-dashboard";

    }

    @GetMapping("/checkout")
    public String checkoutForm(Model model) {
        model.addAttribute("paymentMethods", List.of("COD", "VNPAY", "MOMO"));
        return "checkout";
    }

}
