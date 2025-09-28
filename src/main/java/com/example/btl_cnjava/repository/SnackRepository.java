package com.example.btl_cnjava.repository;

import com.example.btl_cnjava.entity.Snack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface SnackRepository extends JpaRepository<Snack, Long> {
    Page<Snack> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Snack> findByNameContainingIgnoreCase(String keyword);

}
