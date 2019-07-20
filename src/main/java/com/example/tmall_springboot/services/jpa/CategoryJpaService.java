package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.repositories.CategoryRepository;
import com.example.tmall_springboot.services.CategoryService;
import com.example.tmall_springboot.utils.Page4Navigator;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.tmall_springboot.config.Const.NAVIGATE_PAGES;

@Service
@CacheConfig(cacheNames = "categories")
public class CategoryJpaService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryJpaService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Cacheable(key = "'categories-all'")
    public List<Category> getAll() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryRepository.findAll(sort);
    }

    @Override
    @Cacheable(key = "'categories-page-' + #p0 + '-' + #p1")
    public Page4Navigator<Category> pageFromJpa(int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);

        return new Page4Navigator<>(categoryRepository.findAll(pageable), NAVIGATE_PAGES);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Cacheable(key = "'categories-one-' + #p0")
    public Category get(Long id) {
        return categoryRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Category update(Category category) {
        return categoryRepository.save(category);
    }
}
