package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.domains.Property;
import com.example.tmall_springboot.repositories.CategoryRepository;
import com.example.tmall_springboot.repositories.PropertyRepository;
import com.example.tmall_springboot.services.PropertyService;
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
@CacheConfig(cacheNames = "properties")
public class PropertyJpaService implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final CategoryRepository categoryRepository;

    public PropertyJpaService(PropertyRepository propertyRepository, CategoryRepository categoryRepository) {
        this.propertyRepository = propertyRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @CacheEvict(allEntries = true)
    public Property add(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }

    @Override
    @Cacheable(key = "'properties-one-' + #p0")
    public Property get(Long id) {
        return propertyRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Property update(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    @Cacheable(key = "'properties-cid-'+ #p0 + '-page-' + #p1 + '-' + #p2")
    public Page4Navigator<Property> pageFromJpa(Long cid, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Category category = categoryRepository.getOne(cid);

        return new Page4Navigator<>(propertyRepository.findByCategory(category, pageable), NAVIGATE_PAGES);
    }

    @Override
    @Cacheable(key = "'properties-cid-' + #p0.id")
    public List<Property> listByCategory(Category category) {
        return propertyRepository.findByCategory(category);
    }
}
