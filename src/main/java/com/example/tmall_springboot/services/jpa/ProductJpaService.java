package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.repositories.CategoryRepository;
import com.example.tmall_springboot.repositories.ProductRepository;
import com.example.tmall_springboot.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductJpaService implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductJpaService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product add(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product get(Long id) {
        return productRepository.getOne(id);
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Page<Product> pageFromJpa(Long key, int start, int size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Category category = categoryRepository.getOne(key);

        return productRepository.findByCategory(category, pageable);
    }
}
