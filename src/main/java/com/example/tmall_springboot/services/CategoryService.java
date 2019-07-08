package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Category;

import java.util.List;

public interface CategoryService extends PageService<Category> {

    List<Category> getAll();

    Category add(Category category);

    void delete(Long id);

    Category get(Long id);

    Category update(Category category);

}
