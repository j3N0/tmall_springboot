package com.example.tmall_springboot.services;

import com.example.tmall_springboot.domains.Product;
import com.example.tmall_springboot.domains.Property;
import com.example.tmall_springboot.domains.PropertyValue;

import java.util.List;

public interface PropertyValueService {

    PropertyValue update(PropertyValue propertyValue);

    void init(Product product);

    PropertyValue getByPropertyAndProduct(Product product, Property property);

    List<PropertyValue> list(Product product);
}
