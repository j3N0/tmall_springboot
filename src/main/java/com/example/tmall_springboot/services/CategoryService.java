package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends PageService<Category> {

    List<Category> getAll();

    Category add(Category category);

    void delete(Long id);

    Category get(Long id);

    Category update(Category category);

    default void removeCategoryFromProduct(List<Category> cs) {
        cs.forEach(this::removeCategoryFromProduct);
    }

    default void removeCategoryFromProduct(Category category) {
        Optional.ofNullable(category.getProducts())
                .ifPresent(products -> products
                        .forEach(product -> product.setCategory(null)));

        Optional.ofNullable(category.getProductsByRow())
                .ifPresent(productsByRow -> productsByRow
                        .forEach(products -> products
                                .forEach(product -> product.setCategory(null))));
    }

}
