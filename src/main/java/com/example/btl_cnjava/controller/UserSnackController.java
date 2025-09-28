package com.example.btl_cnjava.controller;

import com.example.btl_cnjava.entity.Snack;
import com.example.btl_cnjava.repository.SnackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/snacks")
public class UserSnackController {

    @Autowired
    private SnackRepository snackRepository;

    @GetMapping("/snack-detail/{id}")
    public String getSnackDetail(@PathVariable Long id, Model model) {
        Optional<Snack> optionalSnack = snackRepository.findById(id);
        if (optionalSnack.isPresent()) {
            model.addAttribute("snack", optionalSnack.get());
            return "snack-detail";
        } else {
            return "redirect:/snacks";
        }
    }
}
