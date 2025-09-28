package com.example.btl_cnjava.controller;

import com.example.btl_cnjava.entity.Snack;
import com.example.btl_cnjava.entity.User;
import com.example.btl_cnjava.service.SnackService;
import com.example.btl_cnjava.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private SnackService snackService; // Thêm service snacks vào đây

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        addCsrfToken(model, request);
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Tên người dùng đã tồn tại!");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Mã hóa mật khẩu
        user.setRole("USER");
        userService.save(user);
        return "redirect:/login";
    }

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String showLoginForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        addCsrfToken(model, request);
        return "login";
    }


    // Dashboard của người dùng thường
    @GetMapping("/user-dashboard")
    public String userDashboard(
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model,
            Principal principal,
            HttpServletRequest request) {

        model.addAttribute("username", principal.getName());

        List<Snack> snacks;

        if (keyword == null || keyword.isEmpty()) {
            snacks = snackService.findAll();
        } else {
            snacks = snackService.searchByName(keyword);
        }

        model.addAttribute("snacks", snacks);

        List<String> snackCarouselImages = List.of("slide1.png", "slide2.png", "slide3.png");
        model.addAttribute("snackCarouselImages", snackCarouselImages);

        model.addAttribute("keyword", keyword);

        addCsrfToken(model, request);

        return "user-dashboard";
    }




    // Dashboard của admin
    @GetMapping("/admin/admin-dashboard")
    public String adminDashboard(Model model, Principal principal, HttpServletRequest request) {
        model.addAttribute("username", principal.getName());
        addCsrfToken(model, request);
        return "/admin/admin-dashboard";
    }

    // Hàm tiện ích để thêm CSRF token vào model
    private void addCsrfToken(Model model, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }
    }
}
