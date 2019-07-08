package com.example.tmall_springboot.controllers;

import com.example.tmall_springboot.domains.Order;
import com.example.tmall_springboot.services.OrderItemService;
import com.example.tmall_springboot.services.OrderService;
import com.example.tmall_springboot.utils.Page4Navigator;
import com.example.tmall_springboot.utils.Result;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@RestController
public class OrderController {

    private static final int NAVIGATE_PAGES = 5;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/orders")
    public Page4Navigator<Order> list(@RequestParam(value = "start", defaultValue = "0") int start,
                                      @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        Page<Order> page = orderService.pageFromJpa(start, size);
        orderItemService.fill(page.getContent());
        orderService.removeOrderFromOrderItem(page.getContent());
        return new Page4Navigator<>(page, NAVIGATE_PAGES);
    }

    @PutMapping("deliveryOrder/{oid}")
    public Object deliveryOrder(@PathVariable Long oid) throws IOException {
        Order o = orderService.get(oid);
        o.setDeliveryDate(new Date());
        o.setStatus(OrderService.waitConfirm);
        orderService.update(o);
        return Result.success();
    }
}
