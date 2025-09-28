package com.example.btl_cnjava.controller;

import com.example.btl_cnjava.entity.Snack;
import com.example.btl_cnjava.repository.SnackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/admin/snacks")
public class SnackController {

    @Autowired
    private SnackRepository snackRepository;

    // Hiển thị danh sách snacks với phân trang và tìm kiếm
    @GetMapping
    public String listSnacks(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        int pageSize = 4;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());

        Page<Snack> snackPage;
        if (keyword == null || keyword.trim().isEmpty()) {
            snackPage = snackRepository.findAll(pageable);
        } else {
            snackPage = snackRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
        }

        model.addAttribute("snacks", snackPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", snackPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "/admin/snack-list";
    }

    // Hiển thị form thêm snack mới
    @GetMapping("/add")
    public String showAddSnackForm(Model model) {
        model.addAttribute("snack", new Snack());
        return "/admin/add-snack";
    }

    // Lưu snack mới kèm upload ảnh
    @PostMapping("/save")
    public String saveSnack(@ModelAttribute Snack snack,
                            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            File saveFile = new File(uploadPath, fileName);
            imageFile.transferTo(saveFile);

            snack.setImageName(fileName);
        }

        snackRepository.save(snack);
        return "redirect:/admin/snacks";
    }

    //User

    // Hiển thị chi tiết snack theo id
    @GetMapping("/snack-detail/{id}")
    public String getSnackDetail(@PathVariable Long id, Model model) {
        Optional<Snack> optionalSnack = snackRepository.findById(id);
        if (optionalSnack.isPresent()) {
            model.addAttribute("snack", optionalSnack.get());
            return "snack-detail";
        } else {
            return "redirect:/snacks"; // Hoặc trang lỗi tùy bạn
        }
    }

    // Hiển thị form chỉnh sửa snack
    @GetMapping("/edit/{id}")
    public String editSnack(@PathVariable Long id, Model model) {
        Snack snack = snackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy snack có id " + id));
        model.addAttribute("snack", snack);
        return "/admin/edit-snack";
    }

    // Cập nhật snack theo id kèm upload ảnh
    @PostMapping("/update/{id}")
    public String updateSnack(@PathVariable Long id,
                              @ModelAttribute Snack snack,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        Snack existingSnack = snackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy snack có id " + id));

        existingSnack.setName(snack.getName());
        existingSnack.setDescription(snack.getDescription());
        existingSnack.setPrice(snack.getPrice());
        existingSnack.setCategory(snack.getCategory());
        existingSnack.setStockQuantity(snack.getStockQuantity());
        existingSnack.setInStock(snack.isInStock());

        if (!imageFile.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            File saveFile = new File(uploadPath, fileName);
            imageFile.transferTo(saveFile);

            existingSnack.setImageName(fileName);
        }

        snackRepository.save(existingSnack);
        return "redirect:/admin/snacks";
    }

    // Xóa snack theo id
    @PostMapping("/delete/{id}")
    public String deleteSnack(@PathVariable Long id) {
        snackRepository.deleteById(id);
        return "redirect:/admin/snacks";
    }


}
