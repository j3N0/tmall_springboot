package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Order;
import com.example.tmall_springboot.domains.OrderItem;
import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.repositories.OrderItemRepository;
import com.example.tmall_springboot.services.OrderItemService;
import com.example.tmall_springboot.services.ProductImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemJpaService implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductImageService productImageService;

    public OrderItemJpaService(OrderItemRepository orderItemRepository, ProductImageService productImageService) {
        this.orderItemRepository = orderItemRepository;
        this.productImageService = productImageService;
    }

    @Override
    public void fill(List<Order> orders) {
        orders.forEach(this::fill);
    }

    @Override
    public void fill(Order order) {
        List<OrderItem> orderItems = listByOrder(order);
        //Todo refactor to stream()..
        float total = 0;
        int totalNumber = 0;
        for (OrderItem oi :orderItems) {
            total += oi.getNumber() * oi.getProduct().getPromotePrice();
            totalNumber += oi.getNumber();
            productImageService.setFirstProductImage(oi.getProduct());
        }
        order.setTotal(total);
        order.setOrderItems(orderItems);
        order.setTotalNumber(totalNumber);
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem add(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem get(Long id) {
        return orderItemRepository.getOne(id);
    }

    @Override
    public void delete(Long id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public Integer getSaleCount(Product product) {

        return listByProduct(product).stream()
                .filter(orderItem -> orderItem.getOrder() != null)
                .filter(orderItem -> orderItem.getOrder().getPayDate() != null)
                .map(OrderItem::getNumber)
                .reduce(0, Integer::sum);

    }

    @Override
    public List<OrderItem> listByOrder(Order order) {
        return orderItemRepository.findByOrderOrderByIdDesc(order);
    }

    @Override
    public List<OrderItem> listByProduct(Product product) {
        return orderItemRepository.findByProduct(product);
    }
}
