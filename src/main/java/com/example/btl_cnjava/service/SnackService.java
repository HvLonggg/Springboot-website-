package com.example.btl_cnjava.service;

import com.example.btl_cnjava.entity.Snack;

import java.util.List;

public interface SnackService {
    List<Snack> findAll();
    Snack findById(Long id);
    List<Snack> searchByName(String keyword);
    Snack getById(Long id);
}
