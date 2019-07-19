package com.example.tmall_springboot.controllers;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.services.ProductImageService;
import com.example.tmall_springboot.services.ProductService;
import com.example.tmall_springboot.utils.Page4Navigator;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    public ProductController(ProductService productService, ProductImageService productImageService) {
        this.productService = productService;
        this.productImageService = productImageService;
    }

    @GetMapping("/categories/{cid}/products")
    public Page4Navigator<Product> list(@PathVariable Long cid,
                                        @RequestParam(value = "start", defaultValue = "0") int start,
                                        @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        Page4Navigator<Product> page = productService.pageFromJpa(cid, start, size);

        productImageService.setFirstProductImages(page.getContent());
        return page;
    }

    @GetMapping("/products/{id}")
    public Product get(@PathVariable Long id) throws Exception {
        return productService.get(id);
    }

    @PostMapping("/products")
    public Product add(@RequestBody Product product) throws Exception {
        product.setCreateDate(new Date());
        return productService.add(product);
    }

    @DeleteMapping("/products/{id}")
    public void delete(@PathVariable Long id)  throws Exception {
        productService.delete(id);
    }

    @PutMapping("/products")
    public Product update(@RequestBody Product product) throws Exception {
        return productService.update(product);
    }
}
