package com.example.tmall_springboot.repositories;

import com.example.tmall_springboot.domains.Order;
import com.example.tmall_springboot.domains.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderOrderByIdDesc(Order order);
}
