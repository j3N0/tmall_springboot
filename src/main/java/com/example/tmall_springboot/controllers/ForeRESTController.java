package com.example.tmall_springboot.controllers;

import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.services.CategoryService;
import com.example.tmall_springboot.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ForeRESTController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public ForeRESTController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/forehome")
    public List<Category> home() {
        List<Category> categoryList = categoryService.getAll();
        productService.fill(categoryList);
        productService.fillByRow(categoryList);
        categoryService.removeCategoryFromProduct(categoryList);
        return categoryList;
    }
}
