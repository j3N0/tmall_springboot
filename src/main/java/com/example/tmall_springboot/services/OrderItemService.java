package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Order;
import com.example.tmall_springboot.domains.OrderItem;
import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.User;

import java.util.List;

public interface OrderItemService {

    void fill(List<Order> orders);

    void fill(Order order);

    OrderItem update(OrderItem orderItem);

    OrderItem add(OrderItem orderItem);

    OrderItem get(Long id);

    void delete(Long id);

    Integer getSaleCount(Product product);

    List<OrderItem> listByOrder(Order order);

    List<OrderItem> listByProduct(Product product);

    List<OrderItem> listByUser(User user);
}
