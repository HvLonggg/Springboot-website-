package com.example.btl_cnjava.controller;

import com.example.btl_cnjava.entity.User;
import com.example.btl_cnjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminUserController { // bạn viết sai chính tả: AdminUrerController nên sửa thành AdminUserController
    @Autowired
    private UserService userService;

    @GetMapping("/user_list")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user_list";
    }
}
