package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Category;
import com.example.tmall_springboot.domains.Property;
import com.example.tmall_springboot.repositories.CategoryRepository;
import com.example.tmall_springboot.repositories.PropertyRepository;
import com.example.tmall_springboot.services.PropertyService;
import com.example.tmall_springboot.utils.Page4Navigator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.tmall_springboot.config.Const.NAVIGATE_PAGES;


@Service
public class PropertyJpaService implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final CategoryRepository categoryRepository;

    public PropertyJpaService(PropertyRepository propertyRepository, CategoryRepository categoryRepository) {
        this.propertyRepository = propertyRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Property add(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }

    @Override
    public Property get(Long id) {
        return propertyRepository.getOne(id);
    }

    @Override
    public Property update(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public Page4Navigator<Property> pageFromJpa(Long cid, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Category category = categoryRepository.getOne(cid);

        return new Page4Navigator<>(propertyRepository.findByCategory(category, pageable), NAVIGATE_PAGES);
    }

    @Override
    public List<Property> listByCategory(Category category) {
        return propertyRepository.findByCategory(category);
    }
}
