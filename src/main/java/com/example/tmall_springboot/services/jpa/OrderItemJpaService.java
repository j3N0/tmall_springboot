package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Order;
import com.example.tmall_springboot.domains.OrderItem;
import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.User;
import com.example.tmall_springboot.repositories.OrderItemRepository;
import com.example.tmall_springboot.services.OrderItemService;
import com.example.tmall_springboot.services.ProductImageService;
import com.example.tmall_springboot.utils.SpringContextUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "orderItems")
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
        OrderItemService orderItemService = SpringContextUtil.getBean(OrderItemService.class);

        List<OrderItem> orderItems = orderItemService.listByOrder(order);
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
    @CacheEvict(allEntries = true)
    public OrderItem update(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    @CacheEvict(allEntries = true)
    public OrderItem add(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    @Cacheable(key = "'orderItems-one-' + #p0")
    public OrderItem get(Long id) {
        return orderItemRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public Integer getSaleCount(Product product) {
        OrderItemService orderItemService = SpringContextUtil.getBean(OrderItemService.class);      //AOP

        return orderItemService.listByProduct(product).stream()
                .filter(orderItem -> orderItem.getOrder() != null && orderItem.getOrder().getPayDate() != null)
                .map(OrderItem::getNumber)
                .reduce(0, Integer::sum);

    }

    @Override
    @Cacheable(key = "'orderItems-oid-' + #p0.id")
    public List<OrderItem> listByOrder(Order order) {
        return orderItemRepository.findByOrderOrderByIdDesc(order);
    }

    @Override
    @Cacheable(key = "'orderItems-pid-' + #p0.id")
    public List<OrderItem> listByProduct(Product product) {
        return orderItemRepository.findByProduct(product);
    }

    @Override
    @Cacheable(key = "'orderItems-uid-' + #p0.id")
    public List<OrderItem> listByUser(User user) {
        return orderItemRepository.findByUserAndOrderIsNull(user);
    }
}
