package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Order;
import com.example.tmall_springboot.domains.OrderItem;

import java.util.List;

public interface OrderItemService {

    void fill(List<Order> orders);

    void fill(Order order);

    List<OrderItem> listByOrder(Order order);
}
