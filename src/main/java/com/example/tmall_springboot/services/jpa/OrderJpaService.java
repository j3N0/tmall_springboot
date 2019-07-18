package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Order;
import com.example.tmall_springboot.domains.OrderItem;
import com.example.tmall_springboot.domains.User;
import com.example.tmall_springboot.repositories.OrderRepository;
import com.example.tmall_springboot.services.OrderItemService;
import com.example.tmall_springboot.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderJpaService implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    public OrderJpaService(OrderRepository orderRepository, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }

    @Override
    public void removeOrderFromOrderItem(List<Order> orders) {
        orders.forEach(this::removeOrderFromOrderItem);
    }

    private void removeOrderFromOrderItem(Order order) {
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(null));
    }

    @Override
    public Order get(Long oid) {
        return orderRepository.getOne(oid);
    }

    @Override
    public Order add(Order order) {
        return orderRepository.save(order);
    }


    @Override
    @Transactional(propagation= Propagation.REQUIRED, rollbackForClassName="Exception")
    public float add(Order order, List<OrderItem> orderItems) {

        add(order);

        return orderItems
                .stream()
                .map(orderItem -> {
                    orderItem.setOrder(order);              //update
                    orderItemService.update(orderItem);
                    return orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
                })
                .reduce(0f, Float::sum);
    }

    @Override
    public List<Order> listByUserWithoutDelete(User user) {
        List<Order> orders = orderRepository.findByUserAndStatusNotOrderByIdDesc(user, OrderService.delete);
        orderItemService.fill(orders);
        return orders;
    }

    @Override
    public Order update(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Page<Order> pageFromJpa(int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        return orderRepository.findAll(pageable);
    }
}
