package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Order;

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

    Order update(Order order);

}
