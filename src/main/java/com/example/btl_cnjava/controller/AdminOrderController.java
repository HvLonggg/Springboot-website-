package com.example.btl_cnjava.controller;

import com.example.btl_cnjava.entity.Order;
import com.example.btl_cnjava.service.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private AdminOrderService adminOrderService;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = adminOrderService.getAllOrdersSortedByDateDesc();
        model.addAttribute("orders", orders);
        return "admin/orders";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = adminOrderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/order-detail";
    }

    @PostMapping("/{id}/update-status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes redirectAttributes) {
        adminOrderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái đơn hàng thành công");
        return "redirect:/admin/orders/"+id;
    }
}
