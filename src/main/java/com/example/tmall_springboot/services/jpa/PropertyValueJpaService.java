package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.Property;
import com.example.tmall_springboot.domains.PropertyValue;
import com.example.tmall_springboot.repositories.PropertyRepository;
import com.example.tmall_springboot.repositories.PropertyValueRepository;
import com.example.tmall_springboot.services.PropertyValueService;
import com.example.tmall_springboot.utils.SpringContextUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "propertyValues")
public class PropertyValueJpaService implements PropertyValueService {

    private final PropertyValueRepository propertyValueRepository;
    private final PropertyRepository propertyRepository;

    public PropertyValueJpaService(PropertyValueRepository propertyValueRepository, PropertyRepository propertyRepository) {
        this.propertyValueRepository = propertyValueRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    @CacheEvict(allEntries = true)
    public PropertyValue update(PropertyValue propertyValue) {
        return propertyValueRepository.save(propertyValue);
    }

    @Override
    public void init(Product product) {
        PropertyValueService propertyValueService = SpringContextUtil.getBean(PropertyValueService.class);

        propertyRepository.findByCategory(product.getCategory())
                .stream()
                .filter(property -> propertyValueService.getByPropertyAndProduct(product, property) == null)
                .map(property -> PropertyValue.builder().product(product).property(property).build())
                .forEach(propertyValueRepository::save);
    }

    @Override
    @Cacheable(key = "'propertyValues-one-pid-' + #p0.id + '-ptid-' + #p1.id")
    public PropertyValue getByPropertyAndProduct(Product product, Property property) {
        return propertyValueRepository.getByPropertyAndProduct(property, product);
    }

    @Override
    @Cacheable(key = "'propertyValues-pid-' + #p0.id")
    public List<PropertyValue> list(Product product) {
        return propertyValueRepository.findByProductOrderByIdDesc(product);
    }
}
