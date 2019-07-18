package com.example.tmall_springboot.repositories;

import com.example.tmall_springboot.domains.Order;
import com.example.tmall_springboot.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}
