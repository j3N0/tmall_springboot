package com.example.tmall_springboot.controllers;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.PropertyValue;
import com.example.tmall_springboot.services.ProductService;
import com.example.tmall_springboot.services.PropertyValueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PropertyValueController {

    private final PropertyValueService propertyValueService;
    private final ProductService productService;

    public PropertyValueController(PropertyValueService propertyValueService, ProductService productService) {
        this.propertyValueService = propertyValueService;
        this.productService = productService;
    }

    @GetMapping("/products/{pid}/propertyValues")
    public List<PropertyValue> list(@PathVariable Long pid) {
        Product product = productService.get(pid);
        propertyValueService.init(product);
        return propertyValueService.list(product);
    }

    @PutMapping("/propertyValues")
    public PropertyValue update(@RequestBody PropertyValue propertyValue) {
        System.out.println(propertyValue);
        return propertyValueService.update(propertyValue);
    }
}
