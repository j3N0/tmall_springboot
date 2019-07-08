package com.example.tmall_springboot.repositories;

import com.example.tmall_springboot.domains.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
