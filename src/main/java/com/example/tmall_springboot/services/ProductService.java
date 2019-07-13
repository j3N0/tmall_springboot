package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.domains.Product;

import java.util.List;

public interface ProductService extends PageKeyService<Product> {

    int PRODUCT_NUMBER_EACH_ROW = 8;

    Product add(Product product);

    void delete(Long id);

    Product get(Long id);

    Product update(Product product);

    List<Product> listByCategory(Category category);

    void fill(List<Category> categories);

    void fill(Category category);

    void fillByRow(List<Category> categories);

    void setSaleAndReviewNumber(List<Product> products);

    void setSaleAndReviewNumber(Product product);

    List<Product> search(String keyword, int start, int size);
}
