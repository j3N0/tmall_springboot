package com.example.tmall_springboot.services.jpa;


import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.repositories.CategoryRepository;
import com.example.tmall_springboot.services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryJpaService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryJpaService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryRepository.findAll(sort);
    }

    @Override
    public Page<Category> pageFromJpa(int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);

        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category get(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public Category update(Category category) {
        return categoryRepository.save(category);
    }
}
