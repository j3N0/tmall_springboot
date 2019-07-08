package com.example.tmall_springboot.services.jpa;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.Property;
import com.example.tmall_springboot.domains.PropertyValue;
import com.example.tmall_springboot.repositories.PropertyRepository;
import com.example.tmall_springboot.repositories.PropertyValueRepository;
import com.example.tmall_springboot.services.PropertyValueService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueJpaService implements PropertyValueService {

    private final PropertyValueRepository propertyValueRepository;
    private final PropertyRepository propertyRepository;

    public PropertyValueJpaService(PropertyValueRepository propertyValueRepository, PropertyRepository propertyRepository) {
        this.propertyValueRepository = propertyValueRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public PropertyValue update(PropertyValue propertyValue) {
        return propertyValueRepository.save(propertyValue);
    }

    @Override
    public void init(Product product) {
        propertyRepository.findByCategory(product.getCategory())
                .forEach(property -> {
                    PropertyValue propertyValue = getByPropertyAndProduct(product, property);
                    if (null == propertyValue) {
                        propertyValue = new PropertyValue();
                        propertyValue.setProduct(product);
                        propertyValue.setProperty(property);
                        propertyValueRepository.save(propertyValue);
                    }
                });
        //TODO refactor a better way
    }

    @Override
    public PropertyValue getByPropertyAndProduct(Product product, Property property) {
        return propertyValueRepository.getByPropertyAndProduct(property, product);
    }

    @Override
    public List<PropertyValue> list(Product product) {
        return propertyValueRepository.findByProductOrderByIdDesc(product);
    }
}
