package com.example.btl_cnjava.service;


import com.example.btl_cnjava.entity.Snack;
import com.example.btl_cnjava.repository.SnackRepository;
import com.example.btl_cnjava.service.SnackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SnackServiceImpl implements SnackService {

    @Autowired
    private SnackRepository snackRepository;

    @Override
    public List<Snack> findAll() {
        return snackRepository.findAll();
    }

    @Override
    public List<Snack> searchByName(String keyword) {
        return snackRepository.findByNameContainingIgnoreCase(keyword);
    }
    @Override
    public Snack findById(Long id) {
        // Optional trả về, ta lấy hoặc trả null nếu không tìm thấy
        return snackRepository.findById(id).orElse(null);
    }
    @Override
    public Snack getById(Long id) {
        Optional<Snack> optionalSnack = snackRepository.findById(id);
        return optionalSnack.orElse(null);
    }

}
