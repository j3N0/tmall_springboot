package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Product;

public interface ProductService extends PageKeyService<Product> {

    Product add(Product product);

    void delete(Long id);

    Product get(Long id);

    Product update(Product product);
}
