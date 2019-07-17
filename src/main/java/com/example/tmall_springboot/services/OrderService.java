package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Order;
import com.example.tmall_springboot.domains.OrderItem;

import java.util.List;

public interface OrderService extends PageService<Order> {

    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    void removeOrderFromOrderItem(List<Order> orders);

    Order get(Long oid);

    Order add(Order order);

    float add(Order order, List<OrderItem> orderItems);

    Order update(Order order);

}
