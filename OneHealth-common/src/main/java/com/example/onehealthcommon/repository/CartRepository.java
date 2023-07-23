package com.example.onehealthcommon.repository;

import com.example.onehealthcommon.entity.Cart;
import com.example.onehealthcommon.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findCartByUserId(int id);

}