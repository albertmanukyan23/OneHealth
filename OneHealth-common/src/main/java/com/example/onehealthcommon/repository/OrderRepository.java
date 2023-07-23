package com.example.onehealthcommon.repository;
import com.example.onehealthcommon.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}