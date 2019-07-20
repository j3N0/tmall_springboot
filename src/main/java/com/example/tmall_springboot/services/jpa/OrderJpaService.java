package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Order;
import com.example.tmall_springboot.domains.OrderItem;
import com.example.tmall_springboot.domains.User;
import com.example.tmall_springboot.repositories.OrderRepository;
import com.example.tmall_springboot.services.OrderItemService;
import com.example.tmall_springboot.services.OrderService;
import com.example.tmall_springboot.utils.Page4Navigator;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.tmall_springboot.config.Const.NAVIGATE_PAGES;

@Service
@CacheConfig(cacheNames = "orders")
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

    @Override
    public void removeOrderFromOrderItem(Order order) {
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(null));
    }

    @Override
    @Cacheable(key = "'orders-one-' + #p0")
    public Order get(Long oid) {
        return orderRepository.findById(oid).orElseThrow(RuntimeException::new);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Order add(Order order) {
        return orderRepository.save(order);
    }


    @Override
    @CacheEvict(allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
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
    @Cacheable(key = "'orders-uid-' + #p0.id")
    public List<Order> listByUserWithoutDelete(User user) {
        List<Order> orders = orderRepository.findByUserAndStatusNotOrderByIdDesc(user, OrderService.delete);
        orderItemService.fill(orders);
        return orders;
    }

    @Override
    @CacheEvict(allEntries = true)
    public Order update(Order order) {
        return orderRepository.save(order);
    }

    @Override
    @Cacheable(key = "'orders-page-' + #p0+ '-' + #p1")
    public Page4Navigator<Order> pageFromJpa(int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        return new Page4Navigator<>(orderRepository.findAll(pageable), NAVIGATE_PAGES);
    }
}
